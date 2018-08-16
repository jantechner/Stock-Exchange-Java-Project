package stock;

import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/** Klasa definiująca giełdę */
public class StockExchange extends Market implements Serializable {

    public static final Object updateWatcher = new Object();

    //<editor-fold desc="Lista instancji klasy">

    public static ArrayList<StockExchange> listOfInstances = new ArrayList<>();
    public static StockExchange get(int i) throws IllegalArgumentException {
        if (i >= listOfInstances.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return listOfInstances.get(i);
    }
    public static ArrayList<StockExchange> getList() {
        return listOfInstances;
    }


    //</editor-fold>

    private Country country;
    private String city;
    private String address;
    private Currency currency;
    private Index mainIndex;
    private List<Company> sortedCompanies;

    //<editor-fold desc="Konstruktor i destruktor">

    public StockExchange(String name) throws FileAlreadyExistsException {
        super();
        Market.alreadyExist(name);

        this.name = name;
        mainIndex = new Index("main", this);
        country = Generate.selectCountry();
        currency = Generate.PickCurrency();
        city = Generate.City();
        address = Generate.Address();

        listOfMarkets.add(this);
        listOfInstances.add(this);
        sortedCompanies = new ArrayList<>(mainIndex.getCompanyList());

        for (int i = 0; i < 5; i++) {
            addCompany();
        }
    }

    @Override
    public void remove() {
        if (!listOfMarkets.remove(this))
            throw new NoSuchElementException("Nie ma takiej giełdy do usunięcia!");
        listOfInstances.remove(this);
    }

    //</editor-fold>

    //<editor-fold desc="Gettery">

    public Country getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
    public String getAddress() {
        return address;
    }
    public Currency getCurrency() {
        return currency;
    }
    public Index getMainIndex() { return mainIndex; }
    public List<Company> getSortedCompanies() { return sortedCompanies; }

    //</editor-fold>

    public void addCompany() {
            Company company = new Company();
            company.setStockExchange(this);
            mainIndex.addCompany(company);
            sortedCompanies.add(company);
            updateAll();
            updateBiggestIndexes();
            new Thread(company).start();
    }
    public void removeCompany(Company company) throws NoSuchElementException {
        if (!mainIndex.getCompanyList().contains(company))
            throw new NoSuchElementException("Nie ma takiej spółki do usunięcia z tej giełdy!");
        if(!Company.getList().remove(company))
            throw new NoSuchElementException("Nie znaleziono indeksu podanej spółki na liście wszystkich spółek!");
        removeFromAllIndexes(company);
        sortedCompanies.remove(company);
    }
    public void removeFromAllIndexes(Company company) throws NoSuchElementException {
        mainIndex.removeFromIndex(company);

        if (mainIndex.getCompanyList().size()==0) this.remove();
        else {
            int size = indexList.size();
            for (Index index : indexList) {

                if (index.getMode() != 1) {
                    index.removeFromIndex(company);
                } else if (mainIndex.getCompanyList().size() < index.getCompanyNumber()) {
                    index.setCompanyNumber(mainIndex.getCompanyList().size());
                }

            }

            int i;
            for (i=0; i<size; i++) {
                if (indexList.get(i).getCompanyList().size() == 0) {
                    removeIndex(indexList.get(i));
                    i--;
                    size--;
                }
            }
            updateBiggestIndexes();
        }
    }

    //<editor-fold desc="Lista indeksów na danej giełdzie">

    private ArrayList<Index> indexList = new ArrayList<>();
    public ArrayList<Index> getIndexList() {
        return indexList;
    }
    public Index getIndex(int i) throws IllegalArgumentException {
        if (i >= indexList.size())
            throw new IllegalArgumentException("Argument większy od długości listy!");
        return indexList.get(i);

    }

    private void isOnList(String name) throws NoSuchElementException {
        for (Index i : indexList) {
            if (i.getName().equals(name))
                throw new NoSuchElementException("Podany indeks już jest na tej giełdzie!");
        }
    }

    public void addEmptyIndex(String name) throws NoSuchElementException {
        isOnList(name);
        Index i = new Index(name, this);
        indexList.add(i);
        mainIndex.updateValue();
    }
    public void addIndexWithNewCompanies(String name, int n) throws NoSuchElementException, FileAlreadyExistsException {
        isOnList(name);
        Index i = new Index(name, this, n, 0);
        indexList.add(i);
        mainIndex.updateValue();
    }
    public void addIndexofBiggest(String name, int n) throws NoSuchElementException, FileAlreadyExistsException {
        isOnList(name);
        Index i = new Index(name, this, n, 1);
        indexList.add(i);
        mainIndex.updateValue();
    }

    public void removeIndex(Index index) throws NoSuchElementException, IllegalArgumentException {
        if (index.getName().equals("main"))
            throw new IllegalArgumentException("Nie można usunąć głównego indeksu giełdy!");
        if (!indexList.contains(index))
            throw new NoSuchElementException("Nie ma takiego indeksu do usunięcia na tej giełdzie!");
        indexList.remove(index);
        Index.getList().remove(index);
        this.updateAll();
    }

    //</editor-fold>

    public void updateAll() {

            for (Index i : indexList) {
                i.updateValue();
            }
            mainIndex.updateValue();

        //na wartość indeksu ma wpływ tylko i wyłącznie kurs akcji
    }
    public void updateBiggestIndexes() {

        sortedCompanies.sort(new CompanyComparator());
        for (Index i : indexList) {
            if (i.getMode() == 1) {
                i.setBiggestCompanies(i.getCompanyNumber());
            }
        }
    }


}
