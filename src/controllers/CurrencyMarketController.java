package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import stock.Currency;
import stock.CurrencyMarket;
import stock.Generate;

public class CurrencyMarketController {

    public static class MySet {

        private final Currency currency;
        private final SimpleStringProperty currencyName;
        private final SimpleStringProperty purchasePrice;
        private final SimpleStringProperty offerPrice;

        private MySet(Currency currency, double oprice, double pprice) {
            this.currency = currency;
            this.currencyName = new SimpleStringProperty(currency.getName());
            this.offerPrice = new SimpleStringProperty(((Double) oprice).toString());
            this.purchasePrice = new SimpleStringProperty(((Double) pprice).toString());
        }

        public Currency getCurrency() {
            return currency;
        }

        public String getCurrencyName() {
            return currencyName.get();
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName.set(currencyName);
        }

        public String getOfferPrice() {
            return offerPrice.get();
        }

        public void setOfferPrice(double offerPrice) {
            this.offerPrice.set(((Double) offerPrice).toString());
        }

        public String getPurchasePrice() {
            return purchasePrice.get();
        }

        public void setPurchasePrice(double purchasePrice) {
            this.offerPrice.set(((Double) purchasePrice).toString());
        }


    }

    private ObservableList<Currency> curr = FXCollections.observableArrayList();

    private ObservableList<MySet> data = FXCollections.observableArrayList();

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();

    private final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series2 = new XYChart.Series<>();

    private Currency mainCurrency;

    @FXML
    private javafx.scene.control.ListView<Currency> currList;

    @FXML
    private Label name;

    @FXML
    private Label countries;

    @FXML
    private javafx.scene.control.TableView<MySet> tab;

    @FXML
    private javafx.scene.control.TableColumn<MySet, String> currencyName;

    @FXML
    private javafx.scene.control.TableColumn<MySet, String> offerPrice;

    @FXML
    private javafx.scene.control.TableColumn<MySet, String> purchasePrice;

    @FXML
    private AnchorPane chartPane1;

    @FXML
    private Label relation;

    @FXML
    private Label spread;

    @FXML
    private Label marketName;

    @FXML
    private Label margin;

    @FXML
    private Button addC;

    @FXML
    public void initialize() {
        ButtonsToDisable.addCurrencyMarket = addC;

        currList.setCellFactory(cell ->
                new ListCell<>() {
                    protected void updateItem(Currency item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }

                });

        currList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                showDetails(newValue);
        });

        tab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showCharts(newValue.getCurrency());
            }
        });

        currencyName.setCellValueFactory(
                new PropertyValueFactory<>("currencyName"));

        offerPrice.setCellValueFactory(
                new PropertyValueFactory<>("offerPrice"));

        purchasePrice.setCellValueFactory(
                new PropertyValueFactory<>("purchasePrice"));

        lineChart.setTitle("Quotation");
        lineChart.setAnimated(false);

        chartPane1.getChildren().clear();
        chartPane1.getChildren().add(lineChart);

        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);

        series1.setName("OfferPrice");
        series2.setName("PurchasePrice");

    }

    public void showCurrencies() {

        marketName.setText(CurrencyMarket.get(0).getName());
        margin.setText("Margin : " + CurrencyMarket.get(0).getMargin()*100 + "%");

        curr.clear();
        curr.addAll(Currency.getList());
        currList.setItems(curr);

        tab.getItems().clear();
        tab.setItems(data);

        showDetails(Currency.get(0));
    }

    private void showDetails(Currency currency) {

        mainCurrency = currency;

        name.setText(currency.getName());
        countries.setText(Generate.makeCountryList(currency));

        CurrencyMarket cm = CurrencyMarket.get(0);

        data.clear();
        for (Currency c : Currency.getList()) {
            if (!c.equals(currency)) {
                data.add(new MySet(c, cm.getOfferPrice(currency, c), cm.getPurchasePrice(c, currency)));
            }
        }
        if (!currency.equals(Currency.get(0))) {
            showCharts(Currency.get(0));
        } else {
            showCharts(Currency.get(1));
        }
    }

    private void showCharts(Currency c) {

        relation.setText(mainCurrency.getName() + "/" + c.getName());
        spread.setText("Spread : " + CurrencyMarket.get(0).getSpread(c) + "%");

        int first = Currency.getIndex(mainCurrency);
        int second = Currency.getIndex(c);

        series1.getData().clear();
        int counter = 1;
        for (int i = 0; i < CurrencyMarket.get(0).vList.get(first).get(second).size(); i++) {
            series1.getData().add(new XYChart.Data<>(counter, CurrencyMarket.get(0).vList.get(first).get(second).get(i).get(0)));
            counter++;
        }

        series2.getData().clear();
        counter = 1;
        for (int i = 0; i < CurrencyMarket.get(0).vList.get(first).get(second).size(); i++) {
            series2.getData().add(new XYChart.Data<>(counter, CurrencyMarket.get(0).vList.get(first).get(second).get(i).get(1)));
            counter++;
        }

        lineChart.getData().clear();
        lineChart.getData().addAll(series1, series2);
    }

    @FXML
    private void addCurrency() {
        CurrencyMarket.get(0).addCurrency();
        showCurrencies();
    }

}
