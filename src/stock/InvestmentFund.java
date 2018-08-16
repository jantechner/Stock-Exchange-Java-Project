package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Random;

public class InvestmentFund extends Buyer implements Serializable {

    //<editor-fold desc="Watchers">

    private static final Object freeUnitsWatcher = new Object();
    private static final Object unitValueWatcher = new Object();
    private static final Object generalBudgetWatcher = new Object();

    //</editor-fold>

    //<editor-fold desc="Lista instancji klasy">

    public volatile static ArrayList<InvestmentFund> fundThreads = new ArrayList<>();
    public static ArrayList<InvestmentFund> getFundList() {
        return fundThreads;
    }
    public static InvestmentFund get(int i) throws IllegalArgumentException {
        if (i >= fundThreads.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return fundThreads.get(i);
    }
    private static void alreadyExist(String name) throws FileAlreadyExistsException {
        for (InvestmentFund i : fundThreads) {
            if (i.getName().equals(name))
                throw new FileAlreadyExistsException("");
        }
    }

    //</editor-fold>

    private final int fundUnitNumber;
    private volatile int freeUnits;
    private volatile double generalBudget;
    private volatile double unitValue;

    public InvestmentFund() {
        super();
        setName();
        wallet.setBudget(wallet.getBudget()*10);
        generalBudget = wallet.getBudget();
        fundUnitNumber = (int)(generalBudget / (Generate.Quotation()*2));
        freeUnits = fundUnitNumber;
        unitValue = generalBudget / fundUnitNumber;

        fundThreads.add(this);
        setThreadNumber(fundThreads.size());
    }

    //<editor-fold desc="Metody wymagające synchronizacji">

    public double getGeneralBudget() {
        synchronized (generalBudgetWatcher) {
            return generalBudget;
        }
    }
    public void addToGeneralBudget(double add) {
        synchronized (generalBudgetWatcher) {
            generalBudget += add;
            calculateUnitValue();
        }
    }

    public double getUnitValue() {
        synchronized (unitValueWatcher) {
            return unitValue;
        }
    }
    private void calculateUnitValue() {
        synchronized (unitValueWatcher) {
            unitValue = getGeneralBudget() / fundUnitNumber;
        }
    }

    public int getFreeUnits() {
        synchronized (freeUnitsWatcher) {
            return freeUnits;
        }
    }
    public void addFreeUnits(int units) {
        synchronized (freeUnitsWatcher) {
            this.freeUnits += units;
        }
    }

    //</editor-fold>

    public int getFudnUnitNumber() {
        return fundUnitNumber;
    }
    private void setName() {
        name = Generate.FundName();
        boolean getOut = false;
        do {
            try {
                alreadyExist(name);
                getOut = true;
            } catch (FileAlreadyExistsException e) {
                name = Generate.FundName();
            }
        } while (!getOut);
    }

    //<editor-fold desc="Nadpisane metody nadklasy">

    @Override
    public String getName() {
        return name;
    }
    @Override
    public double getValue() {
        synchronized (unitValueWatcher) {
            return getUnitValue();
        }
    }
    @Override
    public int selectAssetToBuy() {
        return new Random().nextInt(3);
    }
    @Override
    public void setBuyerThreadName(Thread thread) {
        thread.setName("fund" + getThreadNumber());
    }
    @Override
    public void terminate() {
        stop = true;
        getThread().interrupt();
        fundThreads.remove(this);
    }
    @Override
    public String getPesel() {
        return null;
    }
    //</editor-fold>

}
