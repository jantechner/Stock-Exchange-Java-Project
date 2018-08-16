package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/** Klasa definiująca rynki walutowe */
public class CurrencyMarket extends Market implements Serializable {

    public ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> vList = new ArrayList<>();
    public Double getPercentage(double valueNow, int first, int second){
        double initialValue = vList.get(first).get(second).get(0).get(0);
        return ((valueNow - initialValue) / initialValue) * 100.0;
    }

    private static final Object priceWatcher = new Object();
    private static final Object listOfInstancesWatcher = new Object();

    //<editor-fold desc="Lista instancji klasy">

    public static ArrayList<CurrencyMarket> listOfInstances = new ArrayList<>();
    public static CurrencyMarket get(int i) throws IllegalArgumentException {
        if (i >= listOfInstances.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return listOfInstances.get(i);
    }
    public static ArrayList<CurrencyMarket> getList() {
        return listOfInstances;
    }
    public static void removeFromAllCurrencyMarkets(Currency c) {
        for (CurrencyMarket currencyMarket : listOfInstances) {
            currencyMarket.removeCurrency(c);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Konstruktor i destruktor">

    public CurrencyMarket(String name) throws FileAlreadyExistsException {
        super();
        Market.alreadyExist(name);
        this.name = name;
        listOfInstances.add(this);
        listOfMarkets.add(this);
    }

    @Override
    public void remove() throws NoSuchElementException {
        if (!listOfMarkets.remove(this))
            throw new NoSuchElementException("Nie ma takiego rynku walutowego do usunięcia!");
        listOfInstances.remove(this);
    }

    //</editor-fold>

    //<editor-fold desc="Lista walut na danym rynku walutowym">

    private ArrayList<Currency> currencyList = new ArrayList<>();
    public ArrayList<Currency> getCurrencyList() {
        synchronized (listOfInstancesWatcher) {
            return currencyList;
        }
    }
    public Currency addCurrency() throws ArrayStoreException {
        Currency c = new Currency();

        synchronized (priceWatcher) {
            currencyList.add(c);
            spread.add(Generate.Spread());

            //UTRZYMAJ ZGODNOŚĆ KURSÓW

            int size = offerPriceTable.size();
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    offerPriceTable.get(i).add(Generate.OfferPrice());
                } else {
                    offerPriceTable.get(i).add(Generate.RoundDouble(getOfferPrice(0, size) * getOfferPrice(i, 0), 4));
                }
                purchasePriceTable.get(i).add(Generate.CalculatePurchasePrice(spread.get(size), getOfferPrice(i, size)));
            }

            offerPriceTable.add(new ArrayList<>());
            purchasePriceTable.add(new ArrayList<>());

            size = offerPriceTable.size() - 1;
            for (int i = 0; i <= size; i++) {
                if (i == size) {
                    offerPriceTable.get(size).add(0.0);
                } else {
                    offerPriceTable.get(size).add(Generate.RoundDouble(1.0 / getOfferPrice(i, size), 4));
                }
                purchasePriceTable.get(size).add(Generate.CalculatePurchasePrice(spread.get(size), getOfferPrice(size, i)));
            }

            vList.add(new ArrayList<>());

            for (int i = 0; i < vList.get(0).size(); i++) {
                vList.get(vList.size()-1).add(new ArrayList<>());
            }

            for (int i = 0; i < vList.size(); i++) {
                vList.get(i).add(new ArrayList<>());
            }


            return c;
        }
    }
    private void removeCurrency(Currency currency) throws NoSuchElementException {
        if (!currencyList.contains(currency))
            throw new NoSuchElementException("Nie ma takiej waluty do usunięcia!");

        synchronized (priceWatcher) {
            int index = currencyList.indexOf(currency);
            currencyList.remove(currency);

            for (int i = 0; i < offerPriceTable.size(); i++) {
                offerPriceTable.get(i).remove(index);
                purchasePriceTable.get(i).remove(index);
            }
            offerPriceTable.remove(index);
            purchasePriceTable.remove(index);
            spread.remove(index);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Tabela kursów kupna i sprzedaży + spread">
    private volatile ArrayList<Double> spread = new ArrayList<>();
    private volatile ArrayList<ArrayList<Double>> offerPriceTable = new ArrayList<>();
    private volatile ArrayList<ArrayList<Double>> purchasePriceTable = new ArrayList<>();

//    public ArrayList<ArrayList<Double>> getOfferPriceTable() {
//        return offerPriceTable;
//    }
//    public ArrayList<ArrayList<Double>> getPurchasePriceTable() {
//        return purchasePriceTable;
//    }

    public Double getSpread(Currency c) { return spread.get(Currency.getIndex(c)); }

    public double getOfferPrice(int a, int b) {
        return offerPriceTable.get(a).get(b);
    }
    public double getPurchasePrice(int a, int b) {
        return purchasePriceTable.get(b).get(a);
    }

    public double getOfferPrice(Currency a, Currency b) throws IllegalArgumentException {
        synchronized (priceWatcher) {
            if (!currencyList.contains(a) || !currencyList.contains(b))
                throw new IllegalArgumentException("Podanej waluty nie ma w bazie!");
            return getOfferPrice(currencyList.indexOf(a), currencyList.indexOf(b));
        }
    }
    public double getPurchasePrice(Currency a, Currency b) throws IllegalArgumentException {
        synchronized (priceWatcher) {
            if (!currencyList.contains(a) || !currencyList.contains(b))
                throw new IllegalArgumentException("Podanej waluty nie ma w bazie!");
            return getPurchasePrice(currencyList.indexOf(a), currencyList.indexOf(b));
        }
    }

    public void setOfferPrice(int a, int b, double price) throws IllegalArgumentException {
        if (a >= offerPriceTable.size() || b >= offerPriceTable.size())
            throw new IllegalArgumentException("Argument większy od wielkości tablicy");
        offerPriceTable.get(a).set(b, price);
    }
    public void setPurchasePrice(int a, int b, double price) throws IllegalArgumentException {
        if (a >= offerPriceTable.size() || b >= offerPriceTable.size())
            throw new IllegalArgumentException("Argument większy od wielkości tablicy");
        purchasePriceTable.get(a).set(b, price);
    }

    public void changeOfferPrice(Currency a, Currency b, double price) {
        synchronized (priceWatcher) {

            int indexA = currencyList.indexOf(a);
            int indexB = currencyList.indexOf(b);
            int indexMax = Math.max(indexA, indexB);
            setOfferPrice(indexA, indexB, price);

            if (indexA > indexB) {
                setOfferPrice(indexB, indexA, Generate.RoundDouble(1.0 / price, 4));
                int temp = indexA;
                indexA = indexB;
                indexB = temp;
            }
            if (indexA != 0) {
                setOfferPrice(0, indexB, Generate.RoundDouble(getOfferPrice(indexA, indexB) / getOfferPrice(indexA, 0), 4));
                setOfferPrice(indexB, 0, Generate.RoundDouble(1.0 / getOfferPrice(0, indexB), 4));
            }

            setPurchasePrice(0, indexMax, Generate.RoundDouble(Generate.CalculatePurchasePrice(spread.get(indexMax), getOfferPrice(0, indexMax)), 4));
            setPurchasePrice(indexMax, 0, Generate.RoundDouble(Generate.CalculatePurchasePrice(spread.get(0), getOfferPrice(indexMax, 0)), 4));


            int size = offerPriceTable.size();
            int j = 1;
            while (j < size) {

                if (j < indexMax) {
                    setOfferPrice(j, indexMax, Generate.RoundDouble(getOfferPrice(0, indexMax) * getOfferPrice(j, 0), 4));
                    setOfferPrice(indexMax, j, Generate.RoundDouble(1.0 / getOfferPrice(j, indexMax), 4));
                }
                if (j > indexMax) {
                    setOfferPrice(indexMax, j, Generate.RoundDouble(getOfferPrice(0, j) * getOfferPrice(indexMax, 0), 4));
                    setOfferPrice(j, indexMax, Generate.RoundDouble(1.0 / getOfferPrice(indexMax, j), 4));
                }

                setPurchasePrice(j, indexMax, Generate.RoundDouble(Generate.CalculatePurchasePrice(spread.get(indexMax), getOfferPrice(j, indexMax)), 4));
                setPurchasePrice(indexMax, j, Generate.RoundDouble(Generate.CalculatePurchasePrice(spread.get(j), getOfferPrice(indexMax, j)), 4));

                j++;
            }
        }
    }
//    public void changePurchasePrice(Currency a, Currency b, double price) {
//        synchronized (priceWatcher) {
//            int indexB = currencyList.indexOf(a);
//
//            double priceCalculated = Generate.CalculateOfferPrice(spread.get(indexB), price);
//            changeOfferPrice(b, a, priceCalculated);
//        }
//    }

    //</editor-fold

}
