package stock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/** Klasa abstrakcyjna definiująca podmioty
 * wykonujące operacje kupna i sprzedaży - inwestorów
 * i fundusze inwestycyjne
 */
public abstract class Buyer extends Asset implements Runnable, Serializable {

    private static final Object buyCurrencyWatcher = new Object();
    private static final Object buyCommodityWatcher = new Object();
    private static final Object buySharesWatcher = new Object();
    private static final Object buyFundWatcher = new Object();
    private static final Object directSellWatcher = new Object();
    private static final Object wakeUpWatcher = new Object();

    //<editor-fold desc="Lista instancji klasy">

    public volatile static List<Buyer> listOfBuyerInstances = new ArrayList<>();

    public static List<Buyer> getList() {
        return listOfBuyerInstances;
    }

    public static Buyer get(int i) throws IllegalArgumentException {
        if (i >= listOfBuyerInstances.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return listOfBuyerInstances.get(i);
    }

    //</editor-fold>

    private String firstname;
    private String surname;
    protected Wallet wallet = new Wallet();

    public Buyer() {
        super();
        name = null;
        firstname = Generate.FirstName();
        surname = Generate.Surname();
        wallet.setBudget(Generate.Budget());
        wallet.setCurrency(Generate.PickCurrency());
        listOfBuyerInstances.add(this);
    }

    public abstract String getName();

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public double getBudget() {
        return wallet.getBudget();
    }

    public abstract String getPesel();

    public abstract void addToGeneralBudget(double add);

    public abstract int selectAssetToBuy();


    //<editor-fold desc="Nadpisane metody nadklasy">

    @Override
    public Currency getCurrency() {
        return wallet.getCurrency();
    }

    @Override
    public abstract double getValue();

    @Override
    public String getUnit() {
        return null;
    }

    @Override
    public double getMargin() {
        return 1;
    }


    //</editor-fold>

    //<editor-fold desc="Kupno i sprzedaż aktywów">

    private void directSell() {
        if (wallet.getAssetsList().size() != 0) {
            synchronized (directSellWatcher) {
                CurrencyMarket cmarket = CurrencyMarket.get(0);

                boolean sold = false;
                for (int i = 0; i < wallet.getAssetsList().size(); i++) {

                    Asset asset = wallet.getAsset(i);
                    double amount = wallet.getAmountandPrice(i).get(0);
                    double value = wallet.getAmountandPrice(i).get(1);


                    double moneyAfterSell = amount * asset.getValue();
                    if (!wallet.getCurrency().equals(asset.getCurrency())) {
                        moneyAfterSell *= cmarket.getPurchasePrice(asset.getCurrency(), wallet.getCurrency());
                    }

                    double profit = moneyAfterSell - value;
                    if (profit > -50.0) {

                        //im większy profit tym większa szansa na sprzedaż
                        if (new Random().nextInt(1050) < profit + 50) {

                            //gdy widzi zysk sprzedaje cały zasób
                            if (this instanceof InvestmentFund) addToGeneralBudget(profit);

                            if (asset instanceof Currency) {
                                double price = CurrencyMarket.get(0).getOfferPrice(wallet.getCurrency(), ((Currency) asset));
                                CurrencyMarket.get(0).changeOfferPrice(wallet.getCurrency(), ((Currency) asset), (new Random().nextInt(5) + 1) * -0.001 * price + price);
                            }

                            if (asset instanceof Commodity) {
                                ((Commodity) asset).updateValue((new Random().nextInt(5) + 1) * -0.01 * ((Commodity) asset).getValue());
                            }

                            if (asset instanceof Company) {
                                ((Company) asset).addFreeShares((int) amount);
                                ((Company) asset).setCurrentQuotation(-0.01 * (new Random().nextInt(5) + 1) * ((Company) asset).getCurrentQuotation());
                                ((Company) asset).changeTradeVolume((int) amount);

                            }
                            if (asset instanceof InvestmentFund) {
                                ((InvestmentFund) asset).addFreeUnits((int) amount);
                            }


                            moneyAfterSell -= asset.getMargin() * moneyAfterSell;

                            wallet.addBudgetAfterSell(moneyAfterSell);
                            wallet.getAssetsList().remove(i);
                            wallet.getAmountAndPriceList().remove(i);
                            sold = true;
                        }
                    }
                    if (sold) {
                        break;
                    }
                }
            }
        }
    }

    private void directBuy() {
        int mode = selectAssetToBuy();
        try {
            switch (mode) {
                case 0:
                    buyCommodity();
                    break;
                case 1:
                    buyCurrency();
                    break;
                case 2:
                    buyShares();
                    break;
                case 3:
                    buyFundUnit();
                    break;
                default:
                    throw new NoSuchElementException("Nie można wybrać podanego zasobu!");
            }
        } catch (NoSuchElementException e) {
            throw e;
        }

    }

    private void buyCommodity() {
        synchronized (buyCommodityWatcher) {
            if (Commodity.getList().size() != 0) {
                int choice = new Random().nextInt(Commodity.getList().size());
                double commodityValue = Commodity.get(choice).getValue();

                List<Double> result = calculatePriceAndAmount(Commodity.get(choice).getCurrency(), commodityValue);

                if (result != null) {
                    double money = result.get(0) + Commodity.get(choice).getMargin() * result.get(0);

                    wallet.takeBudget(money);
                    wallet.addSet(Commodity.get(choice), result.get(1), result.get(0));

                    Commodity.get(choice).updateValue((new Random().nextInt(5) + 1) * 0.01 * Commodity.get(choice).getValue());
                }
            }
        }
    }

    private void buyCurrency() {
        synchronized (buyCurrencyWatcher) {
            if (Currency.getList().size() != 0) {
                int choice = new Random().nextInt(Currency.getList().size());

                while (Currency.get(choice).equals(wallet.getCurrency())) {
                    choice = new Random().nextInt(Currency.getList().size());
                }

                List<Double> result = calculatePriceAndAmount(Currency.get(choice), 1);

                if (result != null && result.get(1) > 10.0) {

                    double money = result.get(0) + Currency.get(choice).getMargin() * result.get(0);

                    wallet.takeBudget(money);
                    wallet.takeBudget(result.get(0));
                    wallet.addSet(Currency.get(choice), result.get(1), result.get(0));

                    double price = CurrencyMarket.get(0).getOfferPrice(wallet.getCurrency(), Currency.get(choice));
                    CurrencyMarket.get(0).changeOfferPrice(wallet.getCurrency(), Currency.get(choice), (new Random().nextInt(5) + 1) * 0.001 * price + price);
                }
            }
        }
    }

    private void buyShares() {
        synchronized (buySharesWatcher) {
            if (Company.getList().size() != 0) {
                int choice = new Random().nextInt(Company.getList().size());
                double quotation = Company.get(choice).getCurrentQuotation();
                int amount = Company.get(choice).getFreeShares();

                if (amount != 0) {

                    List<Double> result = calculatePriceAndAmount(Company.get(choice).getCurrency(), quotation);

                    if (result != null && result.get(1) > 10) {

                        if (result.get(1) > amount) {
                            result.set(1, amount * 1.0);
                            result.set(0, amount * quotation);
                        }

                        double money = result.get(0) + Company.get(choice).getMargin() * result.get(0);

                        wallet.takeBudget(money);

                        wallet.takeBudget(money);
                        wallet.addSet(Company.get(choice), result.get(1), result.get(0));

                        Company.get(choice).addFreeShares((int) (-1 * result.get(1)));

                        Company.get(choice).changeTradeVolume((int) result.get(1).doubleValue());

                        Company.get(choice).setCurrentQuotation(0.01 * (new Random().nextInt(5) + 1) * Company.get(choice).getCurrentQuotation());

                    }
                }
            }
        }
    }

    private void buyFundUnit() {
        synchronized (buyFundWatcher) {
            if (InvestmentFund.getFundList().size() != 0) {

                int choice = new Random().nextInt(InvestmentFund.getFundList().size());
                double unitValue = InvestmentFund.get(choice).getUnitValue();
                int amount = InvestmentFund.get(choice).getFreeUnits();

                if (amount != 0) {

                    List<Double> result = calculatePriceAndAmount(InvestmentFund.get(choice).wallet.getCurrency(), unitValue);

                    if (result != null) {

                        if (result.get(1) > amount) {
                            result.set(1, amount * 1.0);
                            result.set(0, amount * unitValue);
                        }

                        double money = result.get(0) + InvestmentFund.get(choice).getMargin() * result.get(0);

                        wallet.takeBudget(money);
                        wallet.addSet(InvestmentFund.get(choice), result.get(1), result.get(0));

                        InvestmentFund.get(choice).addFreeUnits((int) (-1 * result.get(1)));
                    }
                }
            }
        }
    }

    private List<Double> calculatePriceAndAmount(Currency currency, double value) {

        List<Double> result = new ArrayList<>();
        CurrencyMarket cmarket = CurrencyMarket.get(0);

        double percentageOfWallet = (new Random().nextInt(7) + 1) / 10.0;
        double offerPrice = 1;
        double budgetInAssetCurrency = wallet.getBudget();

        if (!wallet.getCurrency().equals(currency)) {
            offerPrice = cmarket.getOfferPrice(wallet.getCurrency(), currency);
        }

        budgetInAssetCurrency /= offerPrice;

        double amount = (int) (budgetInAssetCurrency * percentageOfWallet / value);

        if (amount >= 1.0) {
            double price = amount * value * offerPrice;
            result.add(price);
            result.add(amount);
        } else result = null;

        return result;
    }

    //</editor-fold>

    public void remove() {
        if (wallet.getAssetsList().size() != 0) {
            synchronized (directSellWatcher) {
                CurrencyMarket cmarket = CurrencyMarket.get(0);

                for (int i = 0; i < wallet.getAssetsList().size(); i++) {

                    Asset asset = wallet.getAsset(i);
                    double amount = wallet.getAmountandPrice(i).get(0);

                    if (asset instanceof Currency) {
                        double price = CurrencyMarket.get(0).getOfferPrice(wallet.getCurrency(), ((Currency) asset));
                        CurrencyMarket.get(0).changeOfferPrice(wallet.getCurrency(), ((Currency) asset), (new Random().nextInt(5) + 1) * -0.001 * price + price);
                    }

                    if (asset instanceof Commodity) {
                        ((Commodity) asset).updateValue((new Random().nextInt(7) + 1) * -0.01 * ((Commodity) asset).getValue());
                    }

                    if (asset instanceof Company) {
                        ((Company) asset).addFreeShares((int) amount);
                        ((Company) asset).setCurrentQuotation(-0.01 * (new Random().nextInt(8) + 1) * ((Company) asset).getCurrentQuotation());
                        ((Company) asset).changeTradeVolume((int) amount);

                    }
                    if (asset instanceof InvestmentFund) {
                        ((InvestmentFund) asset).addFreeUnits((int) amount);
                    }

                    wallet.getAssetsList().remove(i);
                    wallet.getAmountAndPriceList().remove(i);
                }

                if (this instanceof InvestmentFund) {
                    for (Investor i : Investor.getInvList()) {
                        if (i.getWallet().getAssetsList().contains(this)) {
                            int index = i.getWallet().getAssetsList().indexOf(this);
                            i.getWallet().getAssetsList().remove(index);
                            i.getWallet().getAmountAndPriceList().remove(index);
                        }
                    }
                }
            }
        }
        listOfBuyerInstances.remove(this);
        this.terminate();
    }

    //<editor-fold desc="Mechanizmy wielowątkowości">

    protected volatile boolean stop = false;
    private int threadNumber;
    private Thread currentThread;

    protected Thread getThread() {
        return currentThread;
    }

    @Override
    public void run() {
        currentThread = Thread.currentThread();
        setBuyerThreadName(currentThread);

        while (!stop) {
            if (Timer.action) {
                if (new Random().nextInt(100) < 50)
                    directBuy();
                else
                    directSell();

                waitRandomTime(currentThread, 500);
            } else {
                try {
                    synchronized (currentThread) {
                        currentThread.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
//        System.out.println("Usunięto wątek" + currentThread.getName());
    }

    protected abstract void setBuyerThreadName(Thread thread);

    protected void setThreadNumber(int i) {
        threadNumber = i;
    }

    protected int getThreadNumber() {
        return threadNumber;
    }

    public abstract void terminate();

    protected void waitRandomTime(Thread thread, int timeBound) {
        long time = new Random().nextInt(timeBound);
        try {
            thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    protected void waitExactTime(Thread thread, int exactTime) {
        try {
            thread.sleep(exactTime);
        } catch (InterruptedException e) {
        }
    }

    public void wakeUp() {
        currentThread.interrupt();
    }

    //</editor-fold>

}
