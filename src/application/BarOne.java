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
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A chart that displays rectangular bars with heights indicating data values
 * for categories. Used for displaying information when at least one axis has
 * discontinuous or discrete data.
 */
public class BarOne extends Application {
  DataExtract m = new DataExtract();

  public void setCountryEnergys(HashMap<String, ArrayList<Energy>> countryEnergys) {
    this.countryEnergys = countryEnergys;
  }

  private BarChart chart;
  private CategoryAxis xAxis;
  private NumberAxis yAxis;
  ArrayList<String> years;
  HashMap<String, ArrayList<Energy>> countryEnergys;

  public Parent createContent() throws IOException {
    Group root = new Group();
    m.csvConvert();
    countryEnergys = m.getcountryEnergy();
    years = m.getYearList();
    int n = this.years.size();
    String arrYears[] = new String[n];

    // Copying contents of s to arr[]
    System.arraycopy(years.toArray(), 0, arrYears, 0, n);

    // String[] years = this.years.toArray();
    xAxis = new CategoryAxis();
    xAxis.setCategories(FXCollections.<String>observableArrayList(arrYears));
    yAxis = new NumberAxis("kWh / Person", 0.0d, 130000.0d, 5000.0d);
    ArrayList<BarChart.Series> series = new ArrayList<BarChart.Series>();

    for (Map.Entry<String, ArrayList<Energy>> set : countryEnergys.entrySet()) {
      String country = set.getKey();
      ArrayList<Energy> items = set.getValue();
      ArrayList<BarChart.Data> data = new ArrayList<BarChart.Data>();

      for (Energy item : items) {
        data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));

      }
      series.add(new BarChart.Series(country, FXCollections.observableArrayList(data)));

    }

    ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(series);

    Menu assemble = new Menu("Options");
    final MenuBar menuBar = new MenuBar();
    chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
    VBox vbox = new VBox();
    HBox hbox = new HBox();

    vbox.setMaxSize(900, 600);
    chart.setMaxSize(880, 560);

    hbox.setPadding(new Insets(10, 10, 10, 10));
    vbox.setSpacing(10);
    hbox.setSpacing(10);
    CheckBox cb1 = new CheckBox("All Countries");
    ChoiceBox dropdown = new ChoiceBox();

    dropdown.getItems().addAll(m.getUniqueCountries());
    // ObservableList list = root.getChildren();

    hbox.getChildren().addAll(cb1, dropdown);
    vbox.getChildren().addAll(hbox, chart);

    return vbox;

  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setScene(new Scene(createContent(), 1280, 720 ));
    primaryStage.show();
  }

  /**
   * Java main for when running without JavaFX launcher
   */
  public static void main(String[] args) {
    launch(args);
  }
}