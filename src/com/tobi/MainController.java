package com.tobi;

import com.tobi.print.MyPrinter;
import com.tobi.variogram.VariogramCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.tobi.ReadExcel.*;

public class MainController implements Initializable {

    private Button printButton;
    @FXML
    private Label fileName, close, goBack, distancePairLabel, directionInfoSelection;
    @FXML
    public AnchorPane graphPane, leftAnchor, rootAnchor;
    @FXML
    public Button importButton, calculateVariogramButton;
    @FXML
    public TextField  distanceText;
    @FXML
    public RadioButton westEastRadio, northSouthRadio;
    @FXML
    public TableView<MyVariogramBean> table;
    @FXML
    public TableColumn<MyVariogramBean, Double> variogramCol, pairsCol, distanceCol;

    private boolean WE ;
    private boolean NS ;
    private  AreaChart<Number, Number> areaChart = null;
    private ToggleGroup toggle;
    private File file;
    private List<MyVariogramBean> varPairs = new ArrayList<>() ;
    private double xOffSet;
    private double yOffSet;
    private double GRADEDATA [][] ;
    private int count = 2;
    private boolean state;

    @FXML
    public void onClickAnyThing(ActionEvent event) {
        if (event.getSource() == calculateVariogramButton) {
            int distance = -1;
            try {
                distance= Integer.parseInt(distanceText.getText());
            }catch(Exception e){
                distanceText.clear();
                distancePairLabel.setStyle("-fx-text-fill: red");
                distancePairLabel.setText("Enter a Number !!!");
                return;
            }
            WE = westEastRadio.isSelected();
            NS = northSouthRadio.isSelected();
            if(WE != true && NS != true){
                directionInfoSelection.setStyle("-fx-text-fill: red");
                directionInfoSelection.setText("Please select Direction !!!");
                return;
            }
            if(!state){
                fileName.setStyle("-fx-text-fill: red");
                fileName.setText("Please Import Excel File");
                return;
            }

            if (WE) plotChart(graphPane, VariogramCalculator.Direction.WEST_EAST, file, distance);
            if (NS) plotChart(graphPane, VariogramCalculator.Direction.NOUTH_SOUTH, file, distance);
        }

        if (event.getSource() == importButton) {
            state = true;
            file = FileOpener.chooseFile(Main.window, "choose file...");
            fileName.setStyle("-fx-text-fill: black");
            fileName.setText(file.getName());
            try {
                GRADEDATA = getGrades(file);
            }catch(Exception e) {
                state = false;
                fileName.setStyle("-fx-text-fill: red");
                fileName.setText("Incorrect Data format!!!");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootAnchor.setVisible(true);
        leftAnchor.setVisible(true);
        leftAnchor.setOnMousePressed(event -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        leftAnchor.setOnMouseDragged(event -> {
           Main.window.setX(event.getScreenX() - xOffSet);
           Main.window.setY(event.getScreenY() - yOffSet);
        });
        distanceText.setOnKeyPressed((e)->distancePairLabel.setText(""));
        northSouthRadio.setOnMouseClicked((e)->directionInfoSelection.setText(""));
        westEastRadio.setOnMouseClicked((e)->directionInfoSelection.setText(""));
        toggle = new ToggleGroup();
        northSouthRadio.setToggleGroup(toggle);
        westEastRadio.setToggleGroup(toggle);
    }

    public void plotChart(AnchorPane pane, VariogramCalculator.Direction d, File file, int distance) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Semi Variogram Plot");

        XYChart.Series variogramSeries = new XYChart.Series();
        variogramSeries.setName("Variogram against Distances");
        List<XYChart.Data> chartDatas = new ArrayList<>();

        variogramCol.setCellValueFactory(new PropertyValueFactory<>("variogram"));
        pairsCol.setCellValueFactory(new PropertyValueFactory<>("pairs"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<>("distance"));

        varPairs.clear();

        for (int i = 1; i < count; i++) {
            try {
                String datas[] = VariogramCalculator.calVariogram(GRADEDATA, i , d);
                if(i == 1)
                    count = VariogramCalculator.getCount();
                double x = Double.parseDouble(datas[0]);
                double y = Double.parseDouble(datas[2]);
                double z = Double.parseDouble(datas[1]);
                double xx = x * distance;
                varPairs.add(new MyVariogramBean(y, z, xx));
                chartDatas.add(new XYChart.Data<Number, Number>(xx, y));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        table.setItems(getItems());
        VBox box = new VBox();
        BorderPane borderPane = new BorderPane();
        variogramSeries.getData().addAll(chartDatas);
        areaChart.getData().add(variogramSeries);
        printButton = new Button(" Print ");
        borderPane.setCenter(printButton);
        printButton.setOnAction((e)->printButtonWasClicked());
        // printButton.setStyle("");
        box.getChildren().addAll(areaChart, borderPane);
        pane.getChildren().clear();
        pane.getChildren().add(box);
        pane.setStyle("-fx-background-color: white");
        pane.setVisible(true);

    }

    private ObservableList<MyVariogramBean> getItems() {
        ObservableList<MyVariogramBean> tableItems = FXCollections.observableArrayList();
        tableItems.addAll(varPairs);
        return tableItems;
    }

    @FXML
    private void onClickAnyLabel(MouseEvent event) {
        System.out.println("Label Clicked..");
        if (event.getSource() == close) {
            System.out.println("Label Clicked.. Inside close block");
            Main.window.close();
        }
        if (event.getSource() == goBack) {
            System.out.println("Label Clicked.. Inside home block");
                Scene scene = null;
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("home.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Main.window.setScene(scene);
        }
    }
        private void printButtonWasClicked (){
                try {
                    MyPrinter.print(areaChart, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
}
