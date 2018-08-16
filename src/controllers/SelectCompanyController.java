package controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stock.Company;
import stock.Currency;
import stock.Index;
import stock.StockExchange;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class SelectCompanyController {

    private Company result;
    public static boolean alreadyExecuted = false;
    private final Stage selectWindow = new Stage();
    private ObservableList<Company> list = FXCollections.observableArrayList();
    private Index currentIndex;
    private StockExchange currentStock;


    @FXML
    private ComboBox<Company> select;

    @FXML
    private Pane mainPane;

    @FXML
    private Button okButton;

    @FXML
    private void ok(){
        okButton.setDisable(false);
        currentIndex.addCompany(select.getValue());
        selectWindow.close();
    }

    @FXML
    private void addNew(){
        Company c = new Company();
        c.setStockExchange(currentStock);
        currentStock.getMainIndex().addCompany(c);
        currentIndex.addCompany(c);
        selectWindow.close();
    }

    @FXML
    public void initialize() {

        selectWindow.setScene(new Scene(mainPane));

        selectWindow.initModality(Modality.APPLICATION_MODAL);

        selectWindow.setOnCloseRequest(e -> {
            e.consume();
            selectWindow.close();
        });

        select.getItems().clear();

        select.setCellFactory(cell -> {
            return new ListCell<>() {
                protected void updateItem(Company item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });

        select.setPlaceholder(new Label("Select Company"));

        okButton.setDisable(true);


        select.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                okButton.setDisable(false);
                result = newValue;
                select.setPlaceholder(new Label(newValue.getName()));
            }
        });


    }


    public void select(StockExchange stock, Index i) {
        this.currentIndex = i;
        this.currentStock = stock;
        select.getItems().clear();

        list.clear();
        for (Company c : stock.getMainIndex().getCompanyList()){
            if (!i.getCompanyList().contains(c)){
                list.add(c);
            }
        }
        select.getItems().addAll(list);

        selectWindow.showAndWait();
    }



}
