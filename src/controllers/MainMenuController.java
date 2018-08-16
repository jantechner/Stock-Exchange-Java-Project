package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import stock.Currency;
import stock.CurrencyMarket;

import java.io.IOException;

public class MainMenuController {

    private MainController mainController;
    private CurrencyDetailsController detailsController;

    private ObservableList<Currency> list = FXCollections.observableArrayList();
    private javafx.scene.control.ListView<Currency> listview = new javafx.scene.control.ListView<>();

    private AnchorPane detailsPane = new AnchorPane();

    @FXML
    private Pane mainPane;

    @FXML
    public void initialize() {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/CurrencyDescription.fxml"));
        try {
            detailsPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        detailsController = loader.getController();

        listview.setCellFactory(cell -> {
            return new ListCell<>() {
                protected void updateItem(Currency item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });

        listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showCurrencyDetails(newValue);
            }
        });


    }

    @FXML
    public void CloseProgram() {
        if (controllers.Alert.show() == false) Platform.exit();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void showCurrencies() {
        list.addAll(Currency.getList());
        listview.setItems(list);
        mainController.pane.setCenter(listview);
    }


    @FXML
    public void addCurrency() {
        CurrencyMarket forex = CurrencyMarket.get(0);
        Currency newCurrency = forex.addCurrency();
        list.add(newCurrency);
        listview.setItems(list);
    }

    private void showCurrencyDetails(Currency item) {

        Label name = (Label) detailsPane.lookup("#currencyName");
        name.setText(item.getName());

        mainController.pane.setRight(detailsPane);
    }

}
