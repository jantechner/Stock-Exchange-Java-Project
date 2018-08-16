package stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Random;

/** Klasa definiująca spółki */
public class Company extends Asset implements Runnable, Serializable {

    private static final Object freeSharesWatcher = new Object();
    private static final Object currentQuotationWatcher = new Object();
    private static final Object turnoverAndVolumeWatcher = new Object();
    private static final Object listOfInstancesWatcher = new Object();

    //<editor-fold desc="Lista instancji klasy + synchronizacja">

    public volatile static ArrayList<Company> listOfInstances = new ArrayList<>();

    public static Company get(int i) {
        synchronized (listOfInstancesWatcher) {
            return listOfInstances.get(i);
        }
    }

    public static ArrayList<Company> getList() {
        synchronized (listOfInstancesWatcher) {
            return listOfInstances;
        }
    }

    private static void alreadyExist(String name) {
        for (Company i : listOfInstances) {
            if (i.getName().equals(name))
                throw new NoSuchElementException("Podana spółka już istnieje!");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Atrybuty">

    private Calendar firstQuotationDate;

    private double openingQuotation;
    private volatile double currentQuotation;
    private double minQuotation;
    private double maxQuotation;

    private volatile int sharesNumber;
    private volatile int freeShares;

    private double outturn;
    private double income;

    private double equityCapital;
    private double nominalCapital;

    private volatile int tradeVolume;
    private volatile double turnover;

    private Currency currency;
    private StockExchange stockExchange;


    //</editor-fold>

    public Company() {
        super();
        name = Generate.CompanyName();
        boolean getOut = false;
        do {
            try {
                alreadyExist(name);
                getOut = true;
            } catch (NoSuchElementException e) {
                name = Generate.FirstName();
            }
        } while (!getOut);

        firstQuotationDate = Generate.Date();

        nominalCapital = Generate.Capital();
        equityCapital = nominalCapital;

        sharesNumber = (int) (nominalCapital / Generate.Quotation());
        freeShares = sharesNumber;

        openingQuotation = nominalCapital / sharesNumber;
        currentQuotation = openingQuotation;
        maxQuotation = openingQuotation;
        minQuotation = openingQuotation;

        initialValue = currentQuotation;

        outturn = 0.0;
        income = 0.0;

        currency = Generate.PickCurrency();

        Generate.Buyers("company", sharesNumber);

        tradeVolume = 0;
        turnover = 0.0;
        listOfInstances.add(this);
        threadNumber = listOfInstances.size();
    }

    public void remove() {
        buyBackShares(getCurrentQuotation());
        terminate();
        stockExchange.removeCompany(this);
    }

    //<editor-fold desc="Nadpisane metody nadklasy">

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getValue() {
        synchronized (currentQuotationWatcher) {
            return getCurrentQuotation();
        }
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String getUnit() {
        return null;
    }

    @Override
    public double getMargin() {
        return stockExchange.getMargin();
    }

    //</editor-fold>

    //<editor-fold desc="Zwykłe metody klasy">

    public Calendar getFirstQuotationDate() {

        return firstQuotationDate;
    }

    public double getOpeningQuotation() {
        return openingQuotation;
    }

    public double getMinQuotation() {
        return minQuotation;
    }

    public double getMaxQuotation() {
        return maxQuotation;
    }

    private void calculateOpeningQuotation() {
        this.openingQuotation = getCurrentQuotation();
    }

    public double getOutturn() {
        return outturn;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
        this.outturn = income * 0.77;
        addEquityCapital(this.outturn);
        setCurrentQuotation(outturn / sharesNumber);
    }

    public double getEquityCapital() {
        return equityCapital;
    }

    public double getNominalCapital() {
        return nominalCapital;
    }

    public void addEquityCapital(double add) {
        this.equityCapital += add;
    }


    public StockExchange getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    public int getSharesNumber() {
        return sharesNumber;
    }

    public void addShares(int add) {
        sharesNumber += add;
        addFreeShares(add);
    }

    //</editor-fold>


    //używane przez wiele wątków inwestorów i funduszy inwestycyjnych

    public int getFreeShares() {
        synchronized (freeSharesWatcher) {
            return freeShares;
        }
    }

    public void addFreeShares(int add) {
        synchronized (freeSharesWatcher) {
            this.freeShares += add;
        }
    }

    public double getCurrentQuotation() {
        synchronized (currentQuotationWatcher) {
            return currentQuotation;
        }
    }

    public void setCurrentQuotation(double add) {
        synchronized (currentQuotationWatcher) {
            this.currentQuotation += add;
            if (currentQuotation > maxQuotation) maxQuotation = currentQuotation;
            if (currentQuotation < minQuotation) minQuotation = currentQuotation;
            stockExchange.updateAll();
            stockExchange.updateBiggestIndexes();
        }
    }

    public int getTradeVolume() {
        synchronized (turnoverAndVolumeWatcher) {
            return tradeVolume;
        }
    }

    public void changeTradeVolume(int trade) {
        synchronized (turnoverAndVolumeWatcher) {
            tradeVolume += trade;
            turnover += trade * currentQuotation;
        }
    }

    public double getTurnover() {
        synchronized (turnoverAndVolumeWatcher) {
            return turnover;
        }
    }

    public void buyBackShares(double price) {
        for (Buyer buyer : Buyer.getList()) {

            if (buyer.getWallet().getAssetsList().contains(this)) {

                int index = buyer.getWallet().getIndex(this);

                double amount = buyer.getWallet().getAmountandPrice(index).get(0);
                double moneyAfterSell = amount * price;

                if (!buyer.getWallet().getCurrency().equals(this.getCurrency())) {
                    moneyAfterSell *= CurrencyMarket.get(0).getPurchasePrice(this.getCurrency(), buyer.getWallet().getCurrency());
                }

                //kurs akcji po wykupie rośnie
                this.setCurrentQuotation(0.01 * (new Random().nextInt(8) + 1) * this.getCurrentQuotation());
                this.changeTradeVolume((int) amount);

                buyer.getWallet().addBudgetAfterSell(moneyAfterSell);
                buyer.getWallet().getAssetsList().remove(index);
                buyer.getWallet().getAmountAndPriceList().remove(index);
            }
        }
    }

    //<editor-fold desc="Mechanizmy wielowątkowości">

    protected volatile boolean stop = false;
    private int threadNumber;
    private Thread currentThread;
    private Thread incomeThread;
    private Thread sharesThread;

    public Thread getThread() {
        return currentThread;
    }

    @Override
    public void run() {
        currentThread = Thread.currentThread();
        setThreadName(currentThread);

        runSharesThread();
        runIncomeThread();

    }

    public void setThreadName(Thread thread) {
        thread.setName("comp" + threadNumber);
    }

    public void terminate() {
        stop = true;
        sharesThread.interrupt();
        incomeThread.interrupt();
        currentThread.interrupt();
    }

    ;

    private void runIncomeThread() {
        incomeThread = new Thread() {
            @Override
            public void run() {
                super.run();
                boolean done = true;
                while (!stop) {
                    if (Timer.action) {
                        try {
                            done = false;
                            synchronized (incomeThread) {
                                incomeThread.wait();
                            }
                        } catch (InterruptedException e) {
                        }
                    } else {
                        if (!done) {

                            if (new Random().nextInt(100) < 20) {
                                setIncome(getNominalCapital() * (getNominalCapital() / getEquityCapital()) * 0.01);
                            }
                            calculateOpeningQuotation();

                            synchronized (turnoverAndVolumeWatcher) {
                                tradeVolume = 0;
                                turnover = 0;
                            }
                            done = true;
                        }
                    }
                }
            }
        };
        incomeThread.start();
    }

    private void runSharesThread() {
        sharesThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    synchronized (sharesThread) {
                        sharesThread.wait();
                    }
                } catch (InterruptedException e) {
                }

                while (!stop) {
                    if (Timer.stop) {
                        try {
                            synchronized (sharesThread) {
                                sharesThread.wait();
                            }
                        } catch (InterruptedException e) {
                        }
                    } else {
                        if (!Timer.action) {

                            double procent = (new Random().nextInt(6) + 5) * 0.01;
                            addShares((int) (procent * getSharesNumber()));
                            setCurrentQuotation(getCurrentQuotation() * procent * 0.1);

                            waitRandomTime(sharesThread, 10000, 30000);
                        }
                    }
                }
            }
        };
        sharesThread.start();
    }

    private void waitRandomTime(Thread thread, int lowerBound, int upperBound) {
        long time = new Random().nextInt(upperBound - lowerBound + 1) + lowerBound;
        try {
            thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    public void wakeUpIncome() {
        incomeThread.interrupt();
    }

    public void wakeUpShares() {
        sharesThread.interrupt();
    }

    //</editor-fold>


}
