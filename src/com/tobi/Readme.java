package com.tobi;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Readme {
    static Stage stage = null;

    @FXML
    private  Button okButton;

    @FXML
    public void onClick(){
        stage.close();
    }
}
