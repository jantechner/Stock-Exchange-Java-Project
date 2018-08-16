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
import javafx.scene.layout.Region;
import stock.*;
import java.io.IOException;

public class CommodityMarketsController {

    private CommodityMarket currentMarket;
    private Commodity currentCommodity;

    private AddCommodityMarketController addCommodityMarketController;

    private ObservableList<CommodityMarket> markets = FXCollections.observableArrayList();
    private ObservableList<Commodity> commodities = FXCollections.observableArrayList();

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();

    private final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

    private XYChart.Series<Number, Number> seria = new XYChart.Series<>();


    @FXML
    private javafx.scene.control.ListView<CommodityMarket> commodityMarketList;

    @FXML
    private javafx.scene.control.ListView<Commodity> commodityList;

    @FXML
    private Label marketName;

    @FXML
    private Label margin;

    @FXML
    private Button addCommodity;

    @FXML
    private Button addMarket;

    @FXML
    private Label name;

    @FXML
    private Label currency;

    @FXML
    private Label kurs;

    @FXML
    private Label minkurs;

    @FXML
    private Label maxkurs;

    @FXML
    private AnchorPane chartPane;

    @FXML
    public void initialize() {

        ButtonsToDisable.addCommodity = addCommodity;
        ButtonsToDisable.addCommodityMarket = addMarket;


        commodityMarketList.setCellFactory(cell -> {
            return new ListCell<>() {
                protected void updateItem(CommodityMarket item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });

        commodityList.setCellFactory(ind -> {
            return new ListCell<>() {
                protected void updateItem(Commodity i, boolean empty) {
                    super.updateItem(i, empty);

                    if (i == null || empty) {
                        setText(null);
                    } else {
                        setText(i.getName());
                    }
                }
            };
        });

        commodityMarketList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                showCommodities(newValue);
        });

        commodityList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                showDetails(newValue);
        });

        lineChart.setAnimated(false);

        chartPane.getChildren().clear();
        chartPane.getChildren().add(lineChart);

        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);

    }

    public void showCommodityMarkets() {
        markets.clear();
        markets.addAll(CommodityMarket.getList());
        commodityMarketList.setItems(markets);
        showCommodities(CommodityMarket.get(0));
    }

    private void showCommodities(CommodityMarket market) {
        currentMarket = market;

        marketName.setText(market.getName());
        margin.setText("Margin : " + market.getMargin()*100 + "%");

        commodities.clear();
        commodities.addAll(market.getCommodityList());
        commodityList.setItems(commodities);

        showDetails(market.getCommodityList().get(0));
    }

    public void showDetails(Commodity commodity) {

        name.setText(commodity.getName());
        currency.setText("Currency : " + commodity.getCurrency().getName());
        kurs.setText(((Double)((int)(commodity.getValue() * 100) / 100.0)).toString() + " " + commodity.getCurrency().getName() + " / " +commodity.getUnit() );
        minkurs.setText("MIN : " + ((Double)((int)(commodity.getMinValue() * 100) / 100.0)).toString() + " " + commodity.getCurrency().getName() );
        maxkurs.setText("MAX : " + ((Double)((int)(commodity.getMaxValue() * 100) / 100.0)).toString() + " " + commodity.getCurrency().getName() );

        seria.getData().clear();
        int i = 1;
        for (Double value : commodity.getValuesList()) {
            seria.getData().add(new XYChart.Data<>(i, value));
            i++;
        }

        lineChart.getData().clear();
        lineChart.getData().add(seria);
    }

    @FXML
    public void addCommodity() {
        currentMarket.addCommodity();
        showCommodities(currentMarket);
    }

    @FXML
    public void addCommodityMarket() {

        if (!AddCommodityMarketController.alreadyExecuted) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/AddCommodityMarket.fxml"));
            try {
                loader.load();
                addCommodityMarketController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AddCommodityMarketController.alreadyExecuted = true;
        }

        if(addCommodityMarketController.add()) {
            markets.add(CommodityMarket.get(CommodityMarket.getList().size() - 1));
            commodityMarketList.setItems(markets);
            showCommodities(CommodityMarket.get(CommodityMarket.getList().size() - 1));
        }
    }
}
