package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import stock.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class StockExchangesController {

    private StockExchange currentStock;
    private Index currentIndex;
    private Company currentCompany;

    private SelectCompanyController selectCompanyController;
    private AddStockController addStockController;
    private AddIndexController addIndexController;
    private GetPriceController getPriceController;

    private ObservableList<StockExchange> stocks = FXCollections.observableArrayList();
    private ObservableList<Index> indexes = FXCollections.observableArrayList();
    private ObservableList<Company> companies = FXCollections.observableArrayList();

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<Number, Number> lineChart = new LineChart<>(xAxis,yAxis);
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    @FXML
    private javafx.scene.control.ListView<StockExchange> stockExchangeList;

    @FXML
    private javafx.scene.control.ListView<Index> indexList;

    @FXML
    private javafx.scene.control.ListView<Company> companyList;

    @FXML
    private Label indexName;

    @FXML
    private Label name;

    @FXML
    private Label currency;

    @FXML
    private Label margin;

    @FXML
    private Label address;

    @FXML
    private Label city;

    @FXML
    private Label country;

    @FXML
    private Button addComp;

    @FXML
    private Button addIndex;

    @FXML
    private Button addStockE;

    @FXML
    private Button deleteButton;

    @FXML
    private Label indexResult;

    @FXML
    private Label name1;

    @FXML
    private Label fqd;

    @FXML
    private Label kurso;

    @FXML
    private Label aktkurs;

    @FXML
    private Label kursmin;

    @FXML
    private Label kursmax;

    @FXML
    private Label liczbaa;

    @FXML
    private Label zysk;

    @FXML
    private Label przychod;

    @FXML
    private Label kapw;

    @FXML
    private Label kapz;

    @FXML
    private Label wolumen;

    @FXML
    private Label obroty;

    @FXML
    private AnchorPane chartPane;

    @FXML
    public void initialize() {

        ButtonsToDisable.addCompany = addComp;
        ButtonsToDisable.addIndex = addIndex;
        ButtonsToDisable.deleteCompanyButton = deleteButton;
        ButtonsToDisable.addStockExchange = addStockE;

        stockExchangeList.setCellFactory(cell -> {
            return new ListCell<>() {
                protected void updateItem(StockExchange item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });


        indexList.setCellFactory(ind -> {
            return new ListCell<>() {
                protected void updateItem(Index i, boolean empty) {
                    super.updateItem(i, empty);

                    if (i == null || empty) {
                        setText(null);
                    } else {
                        setText(i.getName());
                    }
                }
            };
        });

        companyList.setCellFactory(com -> {
            return new ListCell<>() {
                protected void updateItem(Company company, boolean empty) {
                    super.updateItem(company, empty);

                    if (company == null || empty) {
                        setText(null);
                    } else {
                        setText(company.getName());
                    }
                }
            };
        });

        indexList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                indexName.setText(newValue.getName());
                indexResult.setText(indVal(newValue));
                if (newValue.getName().equals("main")) {
                    indexName.setText("Main");
                }
                showCompanies(newValue);

                if (newValue.getMode() == 1) {
                    addComp.setDisable(true);
                    ButtonsToDisable.rememberCompanyState = addComp.isDisabled();
                } else if (Timer.stop == false) {
                    addComp.setDisable(true);
                    ButtonsToDisable.rememberCompanyState = addComp.isDisabled();
                } else {
                    addComp.setDisable(false);
                    ButtonsToDisable.rememberCompanyState = addComp.isDisabled();
                }
            }
        });

        stockExchangeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                showIndexes(newValue);
        });

        companyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                showDetails(newValue);
        });

        lineChart.setTitle("Quotation");
        lineChart.setAnimated(false);

        chartPane.getChildren().clear();
        chartPane.getChildren().add(lineChart);

        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);

    }

    public void showStockExchanges() {
        if (!Timer.stop)
            deleteButton.setDisable(true);

        stocks.clear();
        stocks.addAll(StockExchange.getList());
        stockExchangeList.setItems(stocks);
        showIndexes(StockExchange.get(0));
    }

    private void showIndexes(StockExchange stock) {
        currentStock = stock;
        name.setText(stock.getName());
        currency.setText("Currency : " + stock.getCurrency().getName());
        margin.setText("Margin : " + stock.getMargin()*100 + "%");
        address.setText(stock.getAddress());
        city.setText(stock.getCity());
        country.setText(stock.getCountry().getName());

        indexes.clear();
        indexes.add(stock.getMainIndex());
        indexes.addAll(stock.getIndexList());
        indexList.setItems(indexes);
        indexName.setText("Main");
        showCompanies(stock.getMainIndex());
        indexResult.setText(indVal(stock.getMainIndex()));

        stock.updateAll();
        stock.updateBiggestIndexes();
    }

    private void showCompanies(Index index) {
        currentIndex = index;
        companies.clear();
        companies.addAll(index.getCompanyList());
        companyList.setItems(companies);
        if (index.getCompanyList().size() != 0) {
            showDetails(index.getCompanyList().get(0));
        } else {
            showIndexes(currentStock);
        }
    }

    public void showDetails(Company i) {

        if (currentStock.getMainIndex().getCompanyList().size() > 1){
            deleteButton.setDisable(false);
        }

        currentCompany = i;

        String cu = i.getCurrency().getName();

        name1.setText(i.getName());
        String date = i.getFirstQuotationDate().get(Calendar.DAY_OF_MONTH) + ".";
        if (i.getFirstQuotationDate().get(Calendar.MONTH) < 10)
            date += "0";
        date += i.getFirstQuotationDate().get(Calendar.MONTH) + "." + i.getFirstQuotationDate().get(Calendar.YEAR)+"r.";
        fqd.setText("First quotation date : " + date);
        kurso.setText("OPENING : " +(((int)(i.getOpeningQuotation()*1000)) / 1000.0)+"");
        aktkurs.setText((((int)(i.getCurrentQuotation()*1000)) / 1000.0)+" "+cu);
        kursmin.setText("MIN : " +(((int)(i.getMinQuotation()*1000)) / 1000.0)+"");
        kursmax.setText("MAX : " + (((int)(i.getMaxQuotation()*1000)) / 1000.0)+"");
        liczbaa.setText("Shares number : "+i.getSharesNumber()+"");
        zysk.setText("Outturn : " + (((int)(i.getOutturn()*100)) / 100.0)+"");
        przychod.setText("Income : " +(((int)(i.getIncome()*100)) / 100.0)+"");
        kapw.setText("Equity capital : " +(((int)(i.getEquityCapital()*100)) / 100.0)+"");
        kapz.setText("Nominal capital : " +(((int)(i.getNominalCapital()*100)) / 100.0)+"");
        wolumen.setText("Volume : " +i.getTradeVolume()+"");
        obroty.setText("Turnover : "+(int)(i.getTurnover() * 100) /100.0 +"");

        series.getData().clear();
        int counter = 1;
        for (Double value : i.getValuesList()) {
            series.getData().add(new XYChart.Data<>(counter, value));
            counter++;
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    @FXML
    public void addCompany() {

        if (currentIndex.getName().equals("main")) {
            currentStock.addCompany();
        } else {
            if (!SelectCompanyController.alreadyExecuted) {
                FXMLLoader loader2 = new FXMLLoader(this.getClass().getResource("/fxml/SelectCompany.fxml"));
                try {
                    loader2.load();
                    selectCompanyController = loader2.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SelectCompanyController.alreadyExecuted = true;
            }
            selectCompanyController.select(currentStock, currentIndex);
        }
        currentStock.updateAll();
        currentStock.updateBiggestIndexes();
        indexResult.setText(indVal(currentIndex));
        showCompanies(currentIndex);
    }

    @FXML
    public void addStock() {

        if (!AddStockController.alreadyExecuted) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/AddStock.fxml"));
            try {
                loader.load();
                addStockController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AddStockController.alreadyExecuted = true;
        }
        if(addStockController.add()) {

            stocks.add(StockExchange.get(StockExchange.getList().size() - 1));
            stockExchangeList.setItems(stocks);

            showIndexes(StockExchange.get(StockExchange.getList().size()-1));
        }
    }

    @FXML
    public void addInd(){

        if (!AddIndexController.alreadyExecuted) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/AddIndex.fxml"));
            try {
                loader.load();
                addIndexController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AddIndexController.alreadyExecuted = true;
        }
        if(addIndexController.add(currentStock)) {
            currentStock.updateAll();
            currentStock.updateBiggestIndexes();

            showIndexes(currentStock);
        }
    }

    @FXML
    private void buyShares() {
        if (!GetPriceController.alreadyExecuted) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/GetPrice.fxml"));
            try {
                loader.load();
                getPriceController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GetPriceController.alreadyExecuted = true;
        }
        double price = getPriceController.getPrice();
        if(price != -1 ) {
            currentCompany.buyBackShares(price);
            showDetails(currentCompany);
        }
    }

    @FXML
    private void delete(){
        currentCompany.remove();
        showCompanies(currentIndex);
        if (currentStock.getMainIndex().getCompanyList().size() == 1){
            deleteButton.setDisable(true);
        }
    }

    private String indVal(Index index) {
        return ((int) (index.getValue() * 1000) / 1000.0) + "";
    }
}
