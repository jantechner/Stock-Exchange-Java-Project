package controllers;

import javafx.fxml.FXML;

public class ExitAlertController {

    @FXML
    public void PressCancel() {
        Alert.setResult(true);
    }

    @FXML
    public void PressYes() {
        Alert.setResult(false);
    }
}
