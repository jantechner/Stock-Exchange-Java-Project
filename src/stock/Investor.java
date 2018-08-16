package stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/** Klasa definiująca inwestora */
public class Investor extends Buyer implements Serializable {

    //<editor-fold desc="Lista instancji klasy">

    public volatile static ArrayList<Investor> invThreads = new ArrayList<>();
    public static ArrayList<Investor> getInvList() {
        return invThreads;
    }
    public static Investor get(int i) throws IllegalArgumentException {
        if (i >= invThreads.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return invThreads.get(i);
    }

    //</editor-fold>

    private String pesel;

    public Investor() {
        super();
        this.pesel = Generate.Pesel();
        invThreads.add(this);
        setThreadNumber(invThreads.size());
        addBudget();
    }

    //<editor-fold desc="Nadpisane metody nadklasy">

    @Override
    public String getName() {
        return getFirstname() + " " + getSurname();
    }
    @Override
    public double getValue() { return 0.0; }
    @Override
    public void addToGeneralBudget(double add) {}
    @Override
    public int selectAssetToBuy() {
        return new Random().nextInt(4);
    }
    @Override
    public void setBuyerThreadName(Thread thread) {
        thread.setName("inv" + getThreadNumber());
    }
    @Override
    public void terminate() {
        stop = true;
        getThread().interrupt();
        budgetThread.interrupt();
        invThreads.remove(this);
    }
    @Override
    public String getPesel() {
        return pesel;
    }

    //</editor-fold>

    //<editor-fold desc="Mechanizm zwiększania budżetu inwestora">

    private Thread budgetThread;
    private void addBudget() {
        budgetThread = new Thread() {
            @Override
            public void run() {
            super.run();
            while (!stop) {
                wallet.addBudget();
                waitRandomTime(budgetThread, 12000);
            }
            }
        };
        budgetThread.start();
    }

    //</editor-fold>

}
