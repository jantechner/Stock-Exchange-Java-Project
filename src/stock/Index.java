package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

/** Klasa definiująca indeksy giełdowe */
public class Index implements Serializable {

    //<editor-fold desc="Lista instancji klasy">

    private static ArrayList<Index> listOfInstances = new ArrayList<>();
    public static Index get(int i) throws IllegalArgumentException {
        if (i >= listOfInstances.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return listOfInstances.get(i);
    }
    public static ArrayList<Index> getList() {
        return listOfInstances;
    }
    public static void alreadyExist(String name) throws NoSuchElementException {
        for (Index i : listOfInstances) {
            if (i.getName().equals(name))
                throw new NoSuchElementException("Podany indeks giełdowy już istnieje w bazie!");
        }
    }

    //</editor-fold>

    private String name;
    private double value;               // łączna wartość kursów akcji
    private StockExchange baseStock;
    private int companyNumber;          // liczba największych spółek
    private int mode;                   // 0 - zbiór n spółek , 1 - lista n największych spółek

    //<editor-fold desc="Konstruktory i destruktor">

    public Index(String name, StockExchange stock) throws NoSuchElementException {
        alreadyExist(name);
        this.name = name;
        baseStock = stock;
        mode = 0;
        value = 0.0;
        if (!this.name.equals("main")) {
            Generate.Companies(this);
            listOfInstances.add(this);
        }
        updateValue();
    }
    public Index(String name, StockExchange stockExchange, int companyNumber, int mode) throws NoSuchElementException, FileAlreadyExistsException {
        alreadyExist(name);
        this.name = name;
        baseStock = stockExchange;
        this.companyNumber = companyNumber;
        this.mode = mode;
        if (mode == 0) {
            Generate.NewCompanies(this, companyNumber);
        }
        if (mode == 1) {
            setBiggestCompanies(companyNumber);
        }
        listOfInstances.add(this);
        updateValue();
    }

    public void remove() {
        baseStock.removeIndex(this);
    }

    //</editor-fold>

    //<editor-fold desc="Gettery i settery">

    public String getName() {
        return name;
    }
    public double getValue() {
        return value;
    }
    public StockExchange getBaseStock() {
        return baseStock;
    }
    public int getCompanyNumber() {
        return companyNumber;
    }
    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }
    public int getMode() {
        return mode;
    }

    //</editor-fold>

    //<editor-fold desc="Lista spółek w danym indeksie">

    private List<Company> companyList = new ArrayList<>();
    public List<Company> getCompanyList() {
        return companyList;
    }
    public void addCompany(Company c) throws NoSuchElementException {
        if(!name.equals("main")) {
            if (!baseStock.getMainIndex().getCompanyList().contains(c)) {
                throw new NoSuchElementException("Nie można dodać podanej spółki do indeksu, ponieważ nie jest ona zarejestrowana na tej giełdzie!");
            }
        }
        if (companyList.contains(c))
            throw new NoSuchElementException("Podana spółka już jest w tym indeksie!");

        companyList.add(c);
        updateValue();
    }
    public void removeFromIndex(Company c) throws NoSuchElementException, IllegalCallerException {
        if(companyList.contains(c) && mode != 1) {
            companyList.remove(c);
            this.companyNumber--;
            updateValue();
        }
    }

    //</editor-fold>

    public void updateValue() {
        double result = 0.0;
        for (Company c : companyList) {
            result += c.getCurrentQuotation();
        }
        value = result;
    }
    public void setBiggestCompanies(int n) {
        baseStock.getSortedCompanies().sort(new CompanyComparator());
        companyList.clear();
        for (int j = 0; j < n; j++) {
            companyList.add( baseStock.getSortedCompanies().get(j) );
        }
    }
}

