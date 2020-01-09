package com.tobi;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileOpener {

    private static Desktop desktop = Desktop.getDesktop();

    public static void openFile(File file){
        try {
            if(file != null)
                  desktop.open(file);
            else
                System.out.println("i love u");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File chooseFile(Stage stage, String name){
        FileChooser chooser = new FileChooser();
        chooser.setTitle(name);
        chooser.setInitialDirectory(new File("C:/"));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
        File file = chooser.showOpenDialog(stage);
        return file;
    }

    public static List<File> chooseMultipleFile(Stage primaryStage, String select_multiple_files) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(select_multiple_files);
        chooser.setInitialDirectory(new File("C:/"));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
        return chooser.showOpenMultipleDialog(primaryStage);
    }
    public static void browseDirectory(Stage stage, String name){
        DirectoryChooser chooser  = new DirectoryChooser();
        chooser.setTitle(name);
        chooser.setInitialDirectory(new File("C:/users/emmanuel/desktop"));
        File file = chooser.showDialog(stage);
        System.out.println(file.getAbsolutePath());
    }
    public static void saveFile(Stage stage, String name){
        FileChooser chooser  = new FileChooser();
        chooser.setTitle(name);
        chooser.setInitialDirectory(new File("C:/users/emmanuel/desktop"));
        File file = chooser.showSaveDialog(stage);
        System.out.println(file.getAbsolutePath());
    }
}
