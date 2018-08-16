package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Alert {

    private static boolean result = true;
    private static boolean alreadyExecuted = false;
    private static final Stage alertWindow = new Stage();

    public static boolean show() {

        if(!alreadyExecuted) {
            FXMLLoader alert = new FXMLLoader(Alert.class.getResource("/fxml/ExitAlert.fxml"));
            Pane alertPane = null;
            try {
                alertPane = alert.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            alertWindow.initModality(Modality.APPLICATION_MODAL);
            alertWindow.setScene(new Scene(alertPane));

            alreadyExecuted = true;
            alertWindow.setOnCloseRequest(e -> {
                e.consume();
                alertWindow.close();
            });
        }
        alertWindow.showAndWait();
        return result;
    }

    public static void setResult(boolean r){
        result = r;
        alertWindow.close();
    }

}