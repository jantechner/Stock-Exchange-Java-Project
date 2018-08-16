package controllers;
import javafx.stage.Stage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController {

    public BorderPane pane = null;

    private Stage mainStage;

    @FXML
    public Pane mainPane;

    @FXML
    public void initialize(){

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainMenu.fxml"));
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setMainController(this);

        mainPane.getChildren().add(pane);
    }

    @FXML
    public void exit(){
        Platform.exit();
    }


}
