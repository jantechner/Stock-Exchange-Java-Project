package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import stock.*;


public class InvestmentFundController {

    private ObservableList<InvestmentFund> inv = FXCollections.observableArrayList();
    private ObservableList<String> curr = FXCollections.observableArrayList();
    private ObservableList<String> comm = FXCollections.observableArrayList();
    private ObservableList<String> shar = FXCollections.observableArrayList();

    private InvestmentFund currentFund;

    @FXML
    private Label name;

    @FXML
    private Label ownerName;

    @FXML
    private Label budget;

    @FXML
    private Label value;

    @FXML
    private Label totalNumber;

    @FXML
    private Label freeNumber;

    @FXML
    private Button deleteButton;


    @FXML
    private javafx.scene.control.ListView<InvestmentFund> invList;

    @FXML
    private javafx.scene.control.ListView<String> currList;

    @FXML
    private javafx.scene.control.ListView<String> commList;

    @FXML
    private javafx.scene.control.ListView<String> sharList;


    @FXML
    public void initialize() {
        ButtonsToDisable.deleteFundButton = deleteButton;

        invList.setCellFactory(cell -> {
            return new ListCell<>() {
                protected void updateItem(InvestmentFund item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });

        invList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                showDetails(newValue);
        });
    }

    public void showInvestmentFunds() {
        inv.clear();
        inv.addAll(InvestmentFund.getFundList());
        invList.setItems(inv);
        showDetails(InvestmentFund.get(0));

        currList.setItems(curr);
        commList.setItems(comm);
        sharList.setItems(shar);
    }

    public void showDetails(InvestmentFund i) {
        currentFund = i;
        name.setText(i.getName());
        ownerName.setText("Owner : " + i.getFirstname() + " " + i.getSurname());
        budget.setText("Budget : " + (((int)(i.getGeneralBudget() * 100)) / 100.0) + " " + i.getCurrency().getName());
        value.setText((((int)(i.getValue() * 1000)) / 1000.0)+"");
        totalNumber.setText("TOTAL : " + i.getFudnUnitNumber());
        freeNumber.setText(("FREE : " + i.getFreeUnits()));

        curr.clear();
        comm.clear();
        shar.clear();

        Wallet w = i.getWallet();
        for (int j = 0; j < w.getAssetsList().size(); j++) {
            if (w.getAsset(j) instanceof Currency) {
                curr.add(w.getAmountandPrice(j).get(0) + " " + w.getAsset(j).getName());
            } else if (w.getAsset(j) instanceof Commodity) {
                comm.add(w.getAmountandPrice(j).get(0) + " " + w.getAsset(j).getName());
            } else if (w.getAsset(j) instanceof Company) {
                shar.add(w.getAmountandPrice(j).get(0) + " " + w.getAsset(j).getName());
            }
        }
    }
    @FXML
    private void delete() {
        currentFund.remove();
        showInvestmentFunds();
    }
}
