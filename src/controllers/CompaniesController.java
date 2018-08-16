package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import stock.*;

import java.util.ArrayList;
import java.util.Calendar;


public class CompaniesController {

    private ObservableList<Company> comp = FXCollections.observableArrayList();

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();

    private final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    private int selectedItems;

    @FXML
    private javafx.scene.control.ListView<Company> compList;

    @FXML
    private Label name;

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
    private Button show;

    @FXML
    public void initialize() {

        compList.setCellFactory(cell ->
                new ListCell<>() {
                    protected void updateItem(Company item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                });

        compList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        compList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null/* && selectedItems < 2*/) {
                showDetails(newValue);
            }
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

    public void showCompanies() {
        show.setDisable(true);

        comp.clear();
        comp.addAll(Company.getList());
        compList.setItems(comp);

        showDetails(Company.get(0));
    }

    public void showDetails(Company i) {
        String cu = i.getCurrency().getName();

        name.setText(i.getName());
        String date = i.getFirstQuotationDate().get(Calendar.DAY_OF_MONTH) + ".";
        if (i.getFirstQuotationDate().get(Calendar.MONTH) < 10)
            date += "0";
        date += i.getFirstQuotationDate().get(Calendar.MONTH) + "." + i.getFirstQuotationDate().get(Calendar.YEAR) + "r.";
        fqd.setText("First quotation date : " + date);
        kurso.setText("OPENING : " + (((int) (i.getOpeningQuotation() * 1000)) / 1000.0) + "");
        aktkurs.setText((((int) (i.getCurrentQuotation() * 1000)) / 1000.0) + " " + cu);
        kursmin.setText("MIN : " + (((int) (i.getMinQuotation() * 1000)) / 1000.0) + "");
        kursmax.setText("MAX : " + (((int) (i.getMaxQuotation() * 1000)) / 1000.0) + "");
        liczbaa.setText("Shares number : " + i.getSharesNumber() + "");
        zysk.setText("Outturn : " + (((int) (i.getOutturn() * 100)) / 100.0) + "");
        przychod.setText("Income : " + (((int) (i.getIncome() * 100)) / 100.0) + "");
        kapw.setText("Equity capital : " + (((int) (i.getEquityCapital() * 100)) / 100.0) + "");
        kapz.setText("Nominal capital : " + (((int) (i.getNominalCapital() * 100)) / 100.0) + "");
        wolumen.setText("Volume : " + i.getTradeVolume() + "");
        obroty.setText("Turnover : " + (int) (i.getTurnover() * 100) / 100.0 + "");

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
    private void addNew() {
        yAxis.setLabel("");
        selectedItems = compList.getSelectionModel().getSelectedItems().size();
        if (selectedItems >= 2) {
            show.setDisable(false);
        } else {
            show.setDisable(true);
            showDetails(compList.getSelectionModel().getSelectedItems().get(0));
        }
    }

    @FXML
    private void showChart() {
        yAxis.setLabel("% of initial value");
        Company c;
        lineChart.getData().clear();
        int size = compList.getSelectionModel().getSelectedItems().get(0).getValuesList().size();
        for (int i = 0; i < selectedItems; i++) {
            XYChart.Series<Number, Number> multipleSeries = new XYChart.Series<>();
            multipleSeries.setName(compList.getSelectionModel().getSelectedItems().get(i).getName());
            int counter = 1;
            for (int j = 0; j < size; j++) {
                c = compList.getSelectionModel().getSelectedItems().get(i);
                multipleSeries.getData().add(new XYChart.Data<>(counter, c.getPercentage(c.getValuesList().get(j))));
                counter++;
            }
            lineChart.getData().add(multipleSeries);
        }
    }


}
