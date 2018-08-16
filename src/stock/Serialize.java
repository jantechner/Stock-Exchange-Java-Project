package stock;

import java.io.*;
import java.util.ArrayList;

public class Serialize {

    public static void read(){
        try {
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream("serializationData.ser"));

            Buyer.listOfBuyerInstances = (ArrayList<Buyer>) objectIn.readObject();
            Commodity.listOfInstances = (ArrayList<Commodity>) objectIn.readObject();
            CommodityMarket.listOfInstances = (ArrayList<CommodityMarket>) objectIn.readObject();
            Company.listOfInstances = (ArrayList<Company>) objectIn.readObject();
            Currency.listOfInstances = (ArrayList<Currency>) objectIn.readObject();
            CurrencyMarket.listOfInstances = (ArrayList<CurrencyMarket>) objectIn.readObject();
            InvestmentFund.fundThreads = (ArrayList<InvestmentFund>) objectIn.readObject();
            Investor.invThreads = (ArrayList<Investor>) objectIn.readObject();
            Market.listOfMarkets = (ArrayList<Market>) objectIn.readObject();
            StockExchange.listOfInstances = (ArrayList<StockExchange>) objectIn.readObject();
        }
        catch (FileNotFoundException fnfex) {
            fnfex.printStackTrace();
        }
        catch (IOException ioex) {
            ioex.printStackTrace();
        }
        catch (ClassNotFoundException ccex) {
            ccex.printStackTrace();
        }
    }

    public static void write() {

        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream("serializationData.ser"));

            objectOut.writeObject(Buyer.listOfBuyerInstances);
            objectOut.writeObject(Commodity.listOfInstances);
            objectOut.writeObject(CommodityMarket.listOfInstances);
            objectOut.writeObject(Company.listOfInstances);
            objectOut.writeObject(Currency.listOfInstances);
            objectOut.writeObject(CurrencyMarket.listOfInstances);
            objectOut.writeObject(InvestmentFund.fundThreads);
            objectOut.writeObject(Investor.invThreads);
            objectOut.writeObject(Market.listOfMarkets);
            objectOut.writeObject(StockExchange.listOfInstances);

            objectOut.flush();
            objectOut.close();
        }
        catch (FileNotFoundException fnfex) {
            fnfex.printStackTrace();
        }
        catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }


}
