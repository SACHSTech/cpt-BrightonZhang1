package application;
/**
 * Main program for starting program and setting the scene
 * 
 * @author B. Zhang
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Main method runs JavaFX
     * @param args command line argument
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /*
     * Start method
     */
    public void start(Stage primaryStage) throws IOException {
        // Creates new DataExtract object
        DataExtract dataExtract = new DataExtract();

        // csvConvert() extracts csv data from the DataExtract class
        dataExtract.csvRead();

        // Hashmap and arraylist to store coutnry, year, and energy information
        HashMap<String, ArrayList<Energy>> countryEnergy = dataExtract.getcountryEnergy();
        ArrayList<String> years = dataExtract.getYearList();

        // Runs main program
        primaryStage.setScene(new Scene(new Charts(countryEnergy, years).createContent(), 1280, 720));
        primaryStage.show();
    }
}