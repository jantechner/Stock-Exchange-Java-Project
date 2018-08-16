package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import stock.*;


public class InvestorsController {

    private ObservableList<Investor> inv = FXCollections.observableArrayList();
    private ObservableList<String> curr = FXCollections.observableArrayList();
    private ObservableList<String> comm = FXCollections.observableArrayList();
    private ObservableList<String> shar = FXCollections.observableArrayList();
    private ObservableList<String> fund = FXCollections.observableArrayList();

    private Investor currentInvestor;

    @FXML
    private Label name;

    @FXML
    private Label pesel;

    @FXML
    private Label budget;

    @FXML
    private javafx.scene.control.ListView<Investor> invList;

    @FXML
    private javafx.scene.control.ListView<String> currList;

    @FXML
    private javafx.scene.control.ListView<String> commList;

    @FXML
    private javafx.scene.control.ListView<String> sharList;

    @FXML
    private javafx.scene.control.ListView<String> fundList;

    @FXML
    private Button deleteButton;

    @FXML
    public void initialize() {

        ButtonsToDisable.deleteInvestorButton = deleteButton;

        invList.setCellFactory(cell -> {
            return new ListCell<>() {
                protected void updateItem(Investor item, boolean empty) {
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

    public void showInvestors() {
        inv.clear();
        inv.addAll(Investor.getInvList());
        invList.setItems(inv);
        showDetails(Investor.get(0));
    }

    private void showDetails(Investor i) {
        currentInvestor = i;
        name.setText(i.getName());
        pesel.setText("PESEL : " + i.getPesel());
        budget.setText("Budget : " + (((int)(i.getBudget() * 100)) / 100.0) + " " + i.getCurrency().getName());

        curr.clear();
        comm.clear();
        shar.clear();
        fund.clear();

        Wallet w = i.getWallet();
        for (int j = 0; j < w.getAssetsList().size(); j++) {
            if (w.getAsset(j) instanceof Currency) {
                curr.add(w.getAmountandPrice(j).get(0) + " " + w.getAsset(j).getName());
            } else if (w.getAsset(j) instanceof Commodity) {
                comm.add(w.getAmountandPrice(j).get(0) + " " + w.getAsset(j).getName());
            } else if (w.getAsset(j) instanceof Company) {
                shar.add(w.getAmountandPrice(j).get(0) + " " + w.getAsset(j).getName());
            } else if (w.getAsset(j) instanceof InvestmentFund) {
                fund.add(w.getAmountandPrice(j).get(0) + " " + w.getAsset(j).getName());
            }
        }

        currList.setItems(curr);
        commList.setItems(comm);
        sharList.setItems(shar);
        fundList.setItems(fund);
    }

    @FXML
    private void delete(){
        currentInvestor.remove();
        showInvestors();
    }
}
