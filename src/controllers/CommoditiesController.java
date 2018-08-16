package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import stock.Commodity;

public class CommoditiesController {

    private ObservableList<Commodity> comm = FXCollections.observableArrayList();

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();

    private final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    private int selectedItems;

    @FXML
    private javafx.scene.control.ListView<Commodity> commList;

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
    private Button show;


    @FXML
    public void initialize() {

        commList.setCellFactory(cell ->
                new ListCell<>() {
                    protected void updateItem(Commodity item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                });

        commList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        commList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && selectedItems < 2) {
                showDetails(newValue);
            }
        });

        lineChart.setTitle("Price");
        lineChart.setAnimated(false);

        chartPane.getChildren().clear();
        chartPane.getChildren().add(lineChart);

        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);

    }

    public void showCommodities() {

        show.setDisable(true);

        comm.clear();
        comm.addAll(Commodity.getList());
        commList.setItems(comm);

        showDetails(Commodity.get(0));
    }

    private void showDetails(Commodity commodity) {

        name.setText(commodity.getName());
        currency.setText("Currency : " + commodity.getCurrency().getName());
        kurs.setText(((Double) ((int) (commodity.getValue() * 100) / 100.0)).toString() + " " + commodity.getCurrency().getName() + " / " + commodity.getUnit());
        minkurs.setText("MIN : " + ((Double) ((int) (commodity.getMinValue() * 100) / 100.0)).toString() + " " + commodity.getCurrency().getName());
        maxkurs.setText("MAX : " + ((Double) ((int) (commodity.getMaxValue() * 100) / 100.0)).toString() + " " + commodity.getCurrency().getName());

        series.getData().clear();
        int i = 1;
        for (Double value : commodity.getValuesList()) {
            series.getData().add(new XYChart.Data<>(i, value));
            i++;
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    @FXML
    private void addNew() {
        yAxis.setLabel("");
        selectedItems = commList.getSelectionModel().getSelectedItems().size();
        if (selectedItems >= 2) {
            show.setDisable(false);
        } else {
            show.setDisable(true);
            showDetails(commList.getSelectionModel().getSelectedItems().get(0));
        }
    }

    @FXML
    private void showChart() {
        yAxis.setLabel("% of initial value");
        Commodity c;
        lineChart.getData().clear();
        int size = commList.getSelectionModel().getSelectedItems().get(0).getValuesList().size();
        for (int i = 0; i < selectedItems; i++) {
            XYChart.Series<Number, Number> multipleSeries = new XYChart.Series<>();
            multipleSeries.setName(commList.getSelectionModel().getSelectedItems().get(i).getName());
            int counter = 1;
            for (int j = 0; j < size; j++) {
                c = commList.getSelectionModel().getSelectedItems().get(i);
                multipleSeries.getData().add(new XYChart.Data<>(counter, c.getPercentage(c.getValuesList().get(j))));
                counter++;
            }
            lineChart.getData().add(multipleSeries);
        }
    }
}
