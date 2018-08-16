package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/** Klasa definiująca waluty */
public class Currency extends Asset implements Serializable {



    //<editor-fold desc="Lista instancji klasy">

    public static ArrayList<Currency> listOfInstances = new ArrayList<>();
    public static Currency get(int i) throws IllegalArgumentException {
        if (i >= listOfInstances.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return listOfInstances.get(i);
    }
    public static int getIndex(Currency c) { return listOfInstances.indexOf(c); }
    public static ArrayList<Currency> getList() {
        return listOfInstances;
    }
    public static void alreadyExist(String name) throws FileAlreadyExistsException {
        for (Currency i : listOfInstances) {
            if (i.getName().equals(name))
                throw new FileAlreadyExistsException("");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Konstruktor i destruktor">

    public Currency() throws ArrayStoreException {
        super();
        setName();
        Generate.PickCountry(this);
        Generate.Buyers("currency");
        listOfInstances.add(this);
    }

    public void remove() throws NoSuchElementException {
        if(!listOfInstances.remove(this))
            throw new NoSuchElementException("Nie znaleziono podanej waluty do usunięcia!");
        CurrencyMarket.removeFromAllCurrencyMarkets(this);
        Generate.countries.addAll(countryList);
    }

    //</editor-fold>

    //<editor-fold desc="Nadpisane metody nadklasy">

    @Override
    public Currency getCurrency() {
        return this;
    }
    @Override
    public double getValue() {
        return 1.0;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getUnit() {
        return null;
    }
    @Override
    public double getMargin() {
        return CurrencyMarket.get(0).getMargin();

    }

    //</editor-fold>

    private void setName() throws ArrayStoreException {

        if (Currency.listOfInstances.size() > 6000 /*max: 17576*/)
            throw new ArrayStoreException("Nie można utworzyć więcej różnych nazw walut!");

        name = Generate.CurrencyName();
        boolean getOut = false;
        do {
            try {
                alreadyExist(name);
                getOut = true;
            } catch (FileAlreadyExistsException e) {
                name = Generate.CurrencyName();
            }
        } while (!getOut);
    }

    //<editor-fold desc="Lista krajów w jakich obowiązuje dana waluta">

    private ArrayList<Country> countryList = new ArrayList<>();
    public ArrayList<Country> getCountryList() {
        return countryList;
    }
    public void addCountry(Country c) {
        countryList.add(c);
    }
//    public stock.Country getCountry(int i) throws IllegalArgumentException {
//        if (i >= countryList.size())
//            throw new IllegalArgumentException("Argument większy od długości listy!");
//        return countryList.get(i);
//    }

    //</editor-fold>
}