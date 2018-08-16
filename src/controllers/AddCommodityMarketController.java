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
import stock.*;

import java.nio.file.FileAlreadyExistsException;


public class AddCommodityMarketController {

    private String result;
    private boolean added = false;
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
        result = text.getCharacters().toString();
        try {
            new CommodityMarket(result);
            added = true;
            selectWindow.close();
        } catch (FileAlreadyExistsException e) {
            added = false;
            error.setVisible(true);
            okButton.setDisable(true);
            text.setFocusTraversable(false);
        }
    }

    @FXML
    private void retype(){
        text.setPromptText("Commodity Market name");
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
        selectWindow.setTitle("Commodity Market name");

        selectWindow.initModality(Modality.APPLICATION_MODAL);

        selectWindow.setOnCloseRequest(e -> {
            e.consume();
            selectWindow.close();
        });
    }

    public boolean add() {
        okButton.setDisable(true);
        error.setVisible(false);
        text.setFocusTraversable(false);
        text.setText("");
        selectWindow.showAndWait();
        return added;
    }



}
