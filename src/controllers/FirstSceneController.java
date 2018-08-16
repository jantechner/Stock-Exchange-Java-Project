package controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import stock.*;

import java.io.IOException;

public class FirstSceneController {

    private BorderPane CurrenciesPane = null;
    private CurrenciesController currenciesController;

    private BorderPane InvestorsPane = null;
    private InvestorsController investorsController;

    private BorderPane InvestmentFundsPane = null;
    private InvestmentFundController investmentFundsController;

    private BorderPane CompaniesPane = null;
    private CompaniesController companiesController;

    private BorderPane CommoditiesPane = null;
    private CommoditiesController commoditiesController;

    private BorderPane StockExchangesPane = null;
    private StockExchangesController stockExchangesController;

    private BorderPane CommodityMarketsPane = null;
    private CommodityMarketsController commodityMarketsController;

    private BorderPane CurrencyMarketPane = null;
    private CurrencyMarketController currencyMarketController;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button stopB;

    @FXML
    private Button startB;

    @FXML
    private MenuItem saveItem;

    @FXML
    private MenuItem loadItem;

    @FXML
    private void save() {
        Serialize.write();
    }

    @FXML
    private void load() {
        Serialize.read();
    }


    @FXML
    public void startF() {
        Timer.stop = false;
        startB.setDisable(true);
        stopB.setDisable(false);
        for (Company c : Company.getList()){
            c.wakeUpShares();
        }
        Timer.wakeUp();

        ButtonsToDisable.addCompany.setDisable(true);
        ButtonsToDisable.addIndex.setDisable(true);
        ButtonsToDisable.addStockExchange.setDisable(true);
        ButtonsToDisable.addCommodity.setDisable(true);
        ButtonsToDisable.addCommodityMarket.setDisable(true);
        ButtonsToDisable.addCurrencyMarket.setDisable(true);
        ButtonsToDisable.deleteInvestorButton.setDisable(true);
        ButtonsToDisable.deleteFundButton.setDisable(true);
        ButtonsToDisable.deleteCompanyButton.setDisable(true);
        loadItem.setDisable(true);
        saveItem.setDisable(true);

    }

    @FXML
    public void stopF() {
        Timer.stop = true;
        startB.setDisable(false);
        stopB.setDisable(true);
        Timer.wakeUp();

        ButtonsToDisable.addIndex.setDisable(false);
        ButtonsToDisable.addStockExchange.setDisable(false);
        ButtonsToDisable.addCommodity.setDisable(false);
        ButtonsToDisable.addCommodityMarket.setDisable(false);
        ButtonsToDisable.addCurrencyMarket.setDisable(false);
        ButtonsToDisable.addCompany.setDisable(false);

        if (ButtonsToDisable.rememberCompanyState){
            ButtonsToDisable.addCompany.setDisable(true);
        }

        ButtonsToDisable.deleteInvestorButton.setDisable(false);
        ButtonsToDisable.deleteFundButton.setDisable(false);
        ButtonsToDisable.deleteCompanyButton.setDisable(false);



        loadItem.setDisable(false);
        saveItem.setDisable(false);
    }

    @FXML
    public void initialize(){

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Currencies.fxml"));
        try {
            CurrenciesPane = loader.load();
            currenciesController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader1 = new FXMLLoader(this.getClass().getResource("/fxml/Investors.fxml"));
        try {
            InvestorsPane = loader1.load();
            investorsController = loader1.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader2 = new FXMLLoader(this.getClass().getResource("/fxml/InvestmentFunds.fxml"));
        try {
            InvestmentFundsPane = loader2.load();
            investmentFundsController = loader2.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader3 = new FXMLLoader(this.getClass().getResource("/fxml/Companies.fxml"));
        try {
            CompaniesPane = loader3.load();
            companiesController = loader3.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader4 = new FXMLLoader(this.getClass().getResource("/fxml/Commodities.fxml"));
        try {
            CommoditiesPane = loader4.load();
            commoditiesController = loader4.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader5 = new FXMLLoader(this.getClass().getResource("/fxml/StockExchanges.fxml"));
        try {
            StockExchangesPane = loader5.load();
            stockExchangesController = loader5.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader6 = new FXMLLoader(this.getClass().getResource("/fxml/CommodityMarkets.fxml"));
        try {
            CommodityMarketsPane = loader6.load();
            commodityMarketsController = loader6.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader7 = new FXMLLoader(this.getClass().getResource("/fxml/CurrencyMarkets.fxml"));
        try {
            CurrencyMarketPane = loader7.load();
            currencyMarketController = loader7.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @FXML
    public void showStockExchange() {
        mainPane.setCenter(StockExchangesPane);
        stockExchangesController.showStockExchanges();
    }

    @FXML
    public void showCurrencyMarket() {
        mainPane.setCenter(CurrencyMarketPane);
        currencyMarketController.showCurrencies();
    }

    @FXML
    public void showCommodityMarket() {
        mainPane.setCenter(CommodityMarketsPane);
        commodityMarketsController.showCommodityMarkets();
    }

    @FXML
    public void showCurrencies() {
        mainPane.setCenter(CurrenciesPane);
        currenciesController.showCurrencies();
    }

    @FXML
    public void showCommodities() {
        mainPane.setCenter(CommoditiesPane);
        commoditiesController.showCommodities();
    }

    @FXML
    public void showCompanies() {
        mainPane.setCenter(CompaniesPane);
        companiesController.showCompanies();
    }

    @FXML
    public void showInvestors() {
        mainPane.setCenter(InvestorsPane);
        investorsController.showInvestors();

    }

    @FXML
    public void showInvestmentFunds() {
        mainPane.setCenter(InvestmentFundsPane);
        investmentFundsController.showInvestmentFunds();
    }




}
