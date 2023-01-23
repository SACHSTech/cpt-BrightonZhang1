/* ....Show License.... */
package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A chart that displays rectangular bars with heights indicating data values
 * for categories. Used for displaying information when at least one axis has
 * discontinuous or discrete data.
 */
public class BarOne extends Application {
  DataExtract dataExtract = new DataExtract();

  public void setCountryEnergy(HashMap<String, ArrayList<Energy>> countryEnergys) {
    this.countryEnergy = countryEnergys;
  }

  private BarChart chart;
  private CategoryAxis xAxis;
  private NumberAxis yAxis;
  ArrayList<String> years;
  HashMap<String, ArrayList<Energy>> countryEnergy;

  @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = createContent();
        primaryStage.setTitle("Bar Chart Example");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

  public Parent createContent() throws IOException {
    dataExtract.csvConvert();
    countryEnergy = dataExtract.getcountryEnergy();
    years = dataExtract.getYearList();
    int n = years.size();
    String arrYears[] = new String[n];

    TabPane tabPane = new TabPane();
    Tab tab1 = new Tab();
    tab1.setText("Bar Chart");

    // Copying contents of set to array
    System.arraycopy(years.toArray(), 0, arrYears, 0, n);

    xAxis = new CategoryAxis();
    xAxis.setCategories(FXCollections.<String>observableArrayList(arrYears));
    yAxis = new NumberAxis("kWh / Person", 0.0d, 130000.0d, 5000.0d);
    ArrayList<BarChart.Series> series = new ArrayList<BarChart.Series>();

    for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
      String country = set.getKey();

      // Converts values from set to arraylist
      ArrayList<Energy> items = set.getValue();
      ArrayList<BarChart.Data> data = new ArrayList<BarChart.Data>();

      // adds year and energy to data arraylist
      for (Energy item : items) {
        data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));

      }
      // adds barchart data from data arraylist
      series.add(new BarChart.Series(country, FXCollections.observableArrayList(data)));

    }

    ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(series);

    final MenuBar menuBar = new MenuBar();
    chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
    VBox vbox = new VBox();
    HBox hbox = new HBox();

    vbox.setMaxSize(1280, 720);
    chart.setMaxSize(1280, 720);

    hbox.setPadding(new Insets(10, 10, 10, 10));
    vbox.setSpacing(10);
    hbox.setSpacing(10);
    CheckBox cb1 = new CheckBox("All Countries");
    ChoiceBox dropdown = new ChoiceBox();

    dropdown.getItems().addAll(dataExtract.getUniqueCountries());
    // ObservableList list = root.getChildren();

    hbox.getChildren().addAll(cb1, dropdown);
    vbox.getChildren().addAll(menuBar, hbox, chart);
    tab1.setContent(vbox);
    tabPane.getTabs().add(tab1);

    Tab tab2 = new Tab();
    tab2.setText("Tab 2");
    tab2.setContent(new Label("This is the content for another tab"));
    tabPane.getTabs().add(tab2);

    Tab tab3 = new Tab();
    tab3.setText("Tab 3");
    tab3.setContent(new Label("This is the content for another tab"));
    tabPane.getTabs().add(tab3);

    return tabPane;
  }

  /**
   * Java main for when running without JavaFX launcher
   */
  public static void main(String[] args) {
    launch(args);
  }
}