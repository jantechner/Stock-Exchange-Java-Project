import stock.*;

import controllers.Alert;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Javaproject extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FirstScene.fxml"));
        primaryStage.setTitle("Stock Simulator");

        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            if (!Alert.show()) {
                primaryStage.close();
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
        setStartDatabase();
        simulate();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setStartDatabase() {
        try {
            CurrencyMarket forex = new CurrencyMarket("Forex");
            for (int i = 0; i < 7; i++) {
                forex.addCurrency();
            }

            CommodityMarket comm1 = new CommodityMarket("Euronext");
            for (int i = 0; i < 7; i++) {
                comm1.addCommodity();
            }

            CommodityMarket comm2 = new CommodityMarket("Tokyo Commodity Exchange");
            for (int i = 0; i < 3; i++) {
                comm2.addCommodity();
            }

            StockExchange se1 = new StockExchange("NASDAQ");
            se1.addIndexWithNewCompanies("ABX", 8);

            StockExchange gpr = new StockExchange("GPW");
            gpr.addIndexWithNewCompanies("mWIG40", 6);
            gpr.addIndexofBiggest("WIG3", 3);

        } catch (NoSuchElementException e) {
            System.out.println(e);
        } catch (FileAlreadyExistsException e) {
            System.out.println(e);
        }
    }

    public void simulate() {
        try {
            new Timer(3000, 1500);

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    boolean added = false;
                    while (true) {
                        if (Timer.action) {
                            added = false;
                        } else if (!added) {

                            int size;
                            for (int i = 0; i < CurrencyMarket.get(0).vList.size(); i++) {
                                for (int j = 0; j < CurrencyMarket.get(0).vList.size(); j++) {
                                    CurrencyMarket.get(0).vList.get(i).get(j).add(new ArrayList<>());
                                    size = CurrencyMarket.get(0).vList.get(i).get(j).size();
                                    CurrencyMarket.get(0).vList.get(i).get(j).get(size-1).add(CurrencyMarket.get(0).getOfferPrice(i, j));
                                    CurrencyMarket.get(0).vList.get(i).get(j).get(size-1).add(CurrencyMarket.get(0).getPurchasePrice(j, i));
                                }
                            }

                            for (Commodity c : Commodity.getList()) {
                                c.addValue(c.getValue());
                            }

                            for (Company c : Company.getList()) {
                                c.addValue(c.getCurrentQuotation());
                            }
                            added = true;
                        }
                    }

                }
            }.start();

        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
    }


}