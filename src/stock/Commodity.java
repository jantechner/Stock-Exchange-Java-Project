package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/** Klasa definiująca surowce */
public class Commodity extends Asset implements Serializable {

    private static final Object currentValueWatcher = new Object();

    //<editor-fold desc="Lista instancji klasy">

    public static ArrayList<Commodity> listOfInstances = new ArrayList<>();
    public static Commodity get(int i) throws IllegalArgumentException {
        if (i >= listOfInstances.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return listOfInstances.get(i);
    }
    public static ArrayList<Commodity> getList() { return listOfInstances; }
    public static void alreadyExist(String name) throws FileAlreadyExistsException {
        for (Commodity i : listOfInstances) {
            if (i.getName().equals(name))
                throw new FileAlreadyExistsException("Podany surowiec już istnieje w bazie!");
        }
    }

    //</editor-fold>

    private String unit;
    private Currency currency;
    private volatile double value;
    private volatile double minValue;
    private volatile double maxValue;

    //<editor-fold desc="Konstruktor i destruktor">

    public Commodity(){
        super();
        setName();
        setValue(Generate.CommodityValue());
        initialValue = getValue();
        currency = Generate.PickCurrency();
        unit = Generate.PickUnit();

        Generate.Buyers("commodity");
        listOfInstances.add(this);
    }

    public void remove() throws NoSuchElementException {
        if(!listOfInstances.remove(this))
            throw new NoSuchElementException("Nie znaleziono podanego surowca do usunięcia!");
        CommodityMarket.removeFromAllCommodityMarkets(this);
//      stock.Generate.commodities.add(this.getName());
    }

    //</editor-fold>

    //<editor-fold desc="Nadpisane metody nadklasy">

    @Override
    public Currency getCurrency() {
        return currency;
    }
    @Override
    public double getValue() {
        synchronized (currentValueWatcher) {
            return value;
        }
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getUnit() {
        return unit;
    }
    @Override
    public double getMargin() {
        for (CommodityMarket c : CommodityMarket.getList()){
            if (c.getCommodityList().contains(this)){
                return c.getMargin();
            }
        }
        return 1;
    }

    //</editor-fold>

    public void setValue(double price) {
        synchronized (currentValueWatcher) {
            this.value = price;
            maxValue = value;
            minValue = value;
        }
    }
    public void updateValue(double val) {
        synchronized (currentValueWatcher) {
            this.value += val;
            if (value > maxValue) maxValue = this.value;
            if (value < minValue) minValue = this.value;
        }
    }
    public double getMinValue() {
        synchronized (currentValueWatcher) {
            return minValue;
        }
    }
    public double getMaxValue() {
        synchronized (currentValueWatcher) {
            return maxValue;
        }
    }
    private void setName() {
        name = Generate.CommodityName();
        boolean getOut = false;
        do {
            try {
                alreadyExist(name);
                getOut = true;
            } catch (FileAlreadyExistsException e) {
                name = Generate.CommodityName();
            }
        } while (!getOut);
    }
}
