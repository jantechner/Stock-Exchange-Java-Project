package stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Klasa definiująca portfel podmiotów kupujących */
public class Wallet implements Serializable {

    private final Object cartWatcher = new Object();
    private final Object budgetWatcher = new Object();

    //<editor-fold desc="Lista aktywów w portfelu">

    private volatile ArrayList<List<Double>> amountAndPrice = new ArrayList<>();
    private volatile ArrayList<Asset> assets = new ArrayList<>();

    public ArrayList<Asset> getAssetsList() {
        synchronized (cartWatcher) {
            return assets;
        }
    }
    public ArrayList<List<Double>> getAmountAndPriceList() {
        synchronized (cartWatcher) {
            return amountAndPrice;
        }
    }

    public int getIndex(Asset asset) {
        for (int i = 0; i < assets.size() ; i++) {
            if (assets.get(i).equals(asset)){
                return i;
            }
        }
        return 0;
    }
    public Asset getAsset(int i) {
        synchronized (cartWatcher) {
            return assets.get(i);
        }
    }
    public List<Double> getAmountandPrice (int i) {
        synchronized (cartWatcher) {
            return amountAndPrice.get(i);
        }
    }
    public void addSet(Asset asset, double amount, double price) {
        synchronized (cartWatcher) {
            if (assets.contains(asset)) {
                int index = assets.indexOf(asset);
                List<Double> result = new ArrayList<>();
                result.add(amount + amountAndPrice.get(index).get(0));
                result.add(price + amountAndPrice.get(index).get(1));
                amountAndPrice.set(index, result);
            }
            else {
                assets.add(asset);
                List<Double> result = new ArrayList<>();
                result.add(amount);
                result.add(price);
                amountAndPrice.add(result);
            }
        }
    }

    //</editor-fold>

    private volatile double budget;
    private Currency currency;

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    public double getBudget() {
        synchronized (budgetWatcher) {
            return budget;
        }
    }
    public void setBudget(double budget) { this.budget = budget; }
    public void addBudgetAfterSell(double earnings) {
        synchronized (budgetWatcher) {
            budget += earnings;
        }
    }
    public void takeBudget(double price) {
        synchronized (budgetWatcher) {
            budget -= price;
        }
    }
    public void addBudget() {
        synchronized (budgetWatcher) {
            budget += (new Random().nextInt(100000)) / 100.0;
        }
    }
}
