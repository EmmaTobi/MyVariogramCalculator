package com.tobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements  Initializable {

    public Button nextButton, showTips;
    public Label closeLabel;
    public AnchorPane root;
    private double xOffset;
    private double yOffset;

    @FXML
    public void onClick(ActionEvent e){
        if(e.getTarget() == nextButton){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("mainview.fxml"));
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                Main.window.setScene(scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(e.getTarget() == showTips){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("readme.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.setTitle("Readme");
                Readme.stage = stage;
                stage.showAndWait();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeLabel.setOnMouseClicked((e)->Main.window.close());
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            Main.window.setX(event.getScreenX() - xOffset);
            Main.window.setY(event.getScreenY() - yOffset);
        });
    }
}
