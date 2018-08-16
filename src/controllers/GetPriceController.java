package controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stock.Company;
import stock.Currency;
import stock.Index;
import stock.StockExchange;

import java.nio.file.FileAlreadyExistsException;

public class GetPriceController {

    private double result;
    private double price = -1.0;
    public static boolean alreadyExecuted = false;
    private final Stage selectWindow = new Stage();

    @FXML
    private Pane mainPane;

    @FXML
    private TextField text;

    @FXML
    private Button okButton;

    @FXML
    private Label error;

    @FXML
    private void ok(){
        try {
            result = Double.parseDouble(text.getCharacters().toString());
            price = result;
            selectWindow.close();
        } catch (NumberFormatException e) {
            error.setVisible(true);
            okButton.setDisable(true);
            text.setFocusTraversable(false);
        }
    }

    @FXML
    private void retype(){
        text.setPromptText("Set buyback price");
        error.setVisible(false);
    }

    @FXML
    private void typing(){
        if (text.getCharacters().length() == 0) {
            okButton.setDisable(true);
        }
        else okButton.setDisable(false);
    }

    @FXML
    public void initialize() {

        selectWindow.setScene(new Scene(mainPane));
        selectWindow.setTitle("Set buyback price");

        selectWindow.initModality(Modality.APPLICATION_MODAL);

        selectWindow.setOnCloseRequest(e -> {
            e.consume();
            selectWindow.close();
        });
    }

    public double getPrice() {
        okButton.setDisable(true);
        error.setVisible(false);
        text.setFocusTraversable(false);
        text.setText("");
        selectWindow.showAndWait();
        return price;
    }



}
