package stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;


/** Klasa abstrakcyjna przechowująca atrybuty i metody wszystkich aktywów*/
public abstract class Asset extends Object implements Serializable {

    private ArrayList<Double> valuesList = new ArrayList<>();
    public ArrayList<Double> getValuesList() {
        return valuesList;
    }
    public void addValue(double v){ valuesList.add(v); }

    protected String name;
    protected double initialValue;

    public abstract String getName();
    public abstract String getUnit();
    public abstract Currency getCurrency();
    public abstract double getValue();
    public abstract double getMargin();

    public double getPercentage(double valueNow) {
        return ((valueNow - initialValue) / initialValue) * 100.0;
    }

}
