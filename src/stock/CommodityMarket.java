package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/** Klasa definiująca rynki surowców */
public class CommodityMarket extends Market implements Serializable {

    //<editor-fold desc="Lista instancji klasy">

    public static ArrayList<CommodityMarket> listOfInstances = new ArrayList<>();

    public static CommodityMarket get(int i) throws IllegalArgumentException {
        if (i >= listOfInstances.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return listOfInstances.get(i);
    }

    public static ArrayList<CommodityMarket> getList() {
        return listOfInstances;
    }

    public static void removeFromAllCommodityMarkets(Commodity c) {
        for (CommodityMarket commodityMarket : listOfInstances) {
            commodityMarket.removeCommodity(c);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Konstruktor i destruktor">

    public CommodityMarket(String name) throws FileAlreadyExistsException {
        super();
        Market.alreadyExist(name);
        this.name = name;
        listOfInstances.add(this);
        listOfMarkets.add(this);
        for (int i = 0; i < 5; i++) {
            addCommodity();
        }
    }

    @Override
    public void remove() throws NoSuchElementException {
        if (!listOfMarkets.remove(this))
            throw new NoSuchElementException("Nie ma takiego rynku surowców do usunięcia!");
        listOfInstances.remove(this);
    }

    //</editor-fold>

    //<editor-fold desc="Lista surowców na danym rynku surowców">

    private ArrayList<Commodity> commodityList = new ArrayList<>();

    public ArrayList<Commodity> getCommodityList() {
        return commodityList;
    }

    public void addCommodity() {
        Commodity commodity = new Commodity();
        commodityList.add(commodity);
    }

    public void removeCommodity(Commodity commodity) throws NoSuchElementException {
        if (!commodityList.contains(commodity))
            throw new NoSuchElementException("Nie ma takiego surowca do usunięcia!");
        commodityList.remove(commodity);
    }

    //</editor-fold>

}
