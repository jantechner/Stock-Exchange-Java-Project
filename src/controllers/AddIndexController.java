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
import java.util.NoSuchElementException;


public class AddIndexController {

    private String resultName;
    private int resultNumber;
    private int mode;
    private boolean added = false;
    private final Stage selectWindow = new Stage();
    private StockExchange stock;
    public static boolean alreadyExecuted = false;

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
        resultName = text.getCharacters().toString();
        try {
            if (resultName.length() == 0)
                throw new FileAlreadyExistsException("Add index name");

            try {
                Index.alreadyExist(resultName);
            } catch (NoSuchElementException e) {
                throw new FileAlreadyExistsException("Name already exist");
            }

            if (number.getCharacters().length() == 0 && mode != 0)
                throw new FileAlreadyExistsException("Add company number");

            resultNumber = Integer.parseInt(number.getCharacters().toString());

            if (resultNumber > stock.getMainIndex().getCompanyList().size() && mode == 2)
                throw new FileAlreadyExistsException("Number too big");


            added = true;

            if (mode == 0) {
                stock.addEmptyIndex(resultName);
            } else if (mode == 1) {
                stock.addIndexWithNewCompanies(resultName, resultNumber);
            } else {
                stock.addIndexofBiggest(resultName, resultNumber);
            }
            selectWindow.close();
        } catch (FileAlreadyExistsException e) {
            added = false;
            error.setText(e.getMessage());
            okButton.setDisable(true);
            error.setVisible(true);
            text.setFocusTraversable(false);
        }
    }

    @FXML
    private void retype(){
        text.setPromptText("Index name");
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
    private void correctNumber() {
        if (text.getCharacters().toString().length() != 0)
            okButton.setDisable(false);
    }

    @FXML
    private TextField number;

    @FXML
    private RadioButton existing;

    @FXML
    private RadioButton newC;

    @FXML
    private RadioButton biggest;

    @FXML
    private void existingPress(){
        existing.setSelected(true);
        newC.setSelected(false);
        biggest.setSelected(false);
        number.setDisable(true);
        mode = 0;
        number.setFocusTraversable(false);
    }

    @FXML
    private void newPress(){
        newC.setSelected(true);
        existing.setSelected(false);
        biggest.setSelected(false);
        number.setDisable(false);
        mode = 1;
        number.setFocusTraversable(true);

    }

    @FXML
    private void biggestPress(){
        biggest.setSelected(true);
        newC.setSelected(false);
        existing.setSelected(false);
        number.setDisable(false);
        mode = 2;
        number.setFocusTraversable(true);
    }



    @FXML
    public void initialize() {

        selectWindow.setScene(new Scene(mainPane));
        selectWindow.setTitle("Add Index");

        selectWindow.initModality(Modality.APPLICATION_MODAL);

        selectWindow.setOnCloseRequest(e -> {
            e.consume();
            selectWindow.close();
        });
    }

    public boolean add(StockExchange stock) {
        this.stock = stock;

        okButton.setDisable(true);
        error.setVisible(false);

        existing.setSelected(true);
        newC.setSelected(false);
        biggest.setSelected(false);

        number.setFocusTraversable(false);
        number.setText("");

        text.setFocusTraversable(false);
        text.setText("");

        selectWindow.showAndWait();
        return added;
    }



}
