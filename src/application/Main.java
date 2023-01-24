package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        DataExtract dataExtract = new DataExtract();
        dataExtract.csvConvert();
        HashMap<String, ArrayList<Energy>> countryEnergy = dataExtract.getcountryEnergy();
        ArrayList<String> years = dataExtract.getYearList();
        primaryStage.setScene(new Scene(new Charts(countryEnergy, years).createContent(), 1280, 720));
        primaryStage.show();
    }
}