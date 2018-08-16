package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/** Klasa abstrakcyjna definiująca rynek */
public abstract class Market implements Serializable {

    //<editor-fold desc="Lista instancji klasy">

    protected static ArrayList<Market> listOfMarkets = new ArrayList<>();
    public static void alreadyExist(String name) throws FileAlreadyExistsException {
        if (listOfMarkets.size() != 0) {
            for (Market i : listOfMarkets) {
                if (i.getName().equals(name)) {
                    String error = "Istnieje już ";
                    if (i instanceof CurrencyMarket) {
                        error += "rynek walutowy";
                    } else if (i instanceof CommodityMarket) {
                        error += "rynek surowców";
                    } else {
                        error += "giełda";
                    }
                    error += " o podanej nazwie!";
                    throw new FileAlreadyExistsException(error);
                }
            }
        }
    }

    //</editor-fold>

    protected String name;
    private double margin;

    //<editor-fold desc="Konstrunktor i destruktor">

    public Market() throws FileAlreadyExistsException {
        this.margin = Generate.Margin();
    }
    public abstract void remove() throws NoSuchElementException;

    //</editor-fold>

    public String getName() {
        return name;
    }
    public double getMargin() {
        return margin;
    }
}