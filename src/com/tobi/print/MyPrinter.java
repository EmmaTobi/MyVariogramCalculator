package com.tobi.print;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.print.PrinterJob;


public class MyPrinter {

    public static void print( Node node, Stage stage) throws Exception {
            pageSetup(node, stage);
    }

    private static void pageSetup( Node node, Stage primaryStage) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if(printerJob == null){
            return;
        }
        boolean proceed = printerJob.showPrintDialog(primaryStage);
        if(proceed)
            print(printerJob, node);
    }

    private static  void print(PrinterJob job, Node node) {
        boolean printed = job.printPage(node);
        if (printed)
            job.endJob();
    }
}
