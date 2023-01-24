package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Charts {
    private HashMap<String, ArrayList<Energy>> countryEnergy;
    private ArrayList<String> years;
    private BarChart chart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    public Charts(HashMap<String, ArrayList<Energy>> countryEnergy, ArrayList<String> years) {
        this.countryEnergy = countryEnergy;
        this.years = years;
    }

    public Parent createContent() throws IOException {
        int n = years.size();
        String arrYears[] = new String[n];

        // Copies contents of the set to array
        System.arraycopy(years.toArray(), 0, arrYears, 0, n);

        // Bar chart code
        ArrayList<BarChart.Series> series = new ArrayList<BarChart.Series>();

        xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(arrYears));
        yAxis = new NumberAxis("kWh / Person", 0.0d, 130000.0d, 5000.0d);
        xAxis.setLabel("Years (2000 - 2021)");

        for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
          String country = set.getKey();

          // Converts values from set to arraylist
          ArrayList<Energy> items = set.getValue();
          ArrayList<BarChart.Data> data = new ArrayList<BarChart.Data>();

          // adds year and energy to data array
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
      
        Label titleOne = new Label("Energy Consumption Bar Graph");
        titleOne.setStyle("-fx-font-size: 30px;");
        titleOne.setPadding(new Insets(0,0,-90, 460));
        vbox.getChildren().add(0, titleOne);
      
        vbox.setMaxSize(1280, 720);
        chart.setMaxSize(1280, 720);
      
        hbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        hbox.setSpacing(10);
        
        CheckBox joulesCheckbox = new CheckBox("View in Joules");
        joulesCheckbox.setPadding(new Insets(0, 0, 0, 75));
        joulesCheckbox.setSelected(false);
      
        // Store the original upper and lower bounds of the y-axis
        double originalUpperBound = yAxis.getUpperBound();
        double originalLowerBound = yAxis.getLowerBound();
      
        // variable to store the original tick unit of the y-axis
        double originalTickUnit = yAxis.getTickUnit();
      
        joulesCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Convert the upper and lower bounds of the y-axis
                yAxis.setUpperBound(originalUpperBound * 3600000);
                yAxis.setLowerBound(originalLowerBound * 3600000);
                yAxis.setLabel("Joules / Person");
      
                // Sets ticks for Joules
                yAxis.setTickUnit(originalTickUnit * 3600000);
                for (XYChart.Series<String, Number> s : (ObservableList<XYChart.Series<String, Number>>) chart.getData()) {
                    for (XYChart.Data<String, Number> d : s.getData()) {
                        d.setYValue(d.getYValue().doubleValue() * 3600000);
                    }
                }
            } 
            
            else {
                yAxis.setUpperBound(originalUpperBound);
                yAxis.setLowerBound(originalLowerBound);
                yAxis.setLabel("kWh / Person");
                yAxis.setTickUnit(originalTickUnit);
                for (XYChart.Series<String, Number> s : (ObservableList<XYChart.Series<String, Number>>) chart.getData()) {
                    for (XYChart.Data<String, Number> d : s.getData()) {
                        d.setYValue(d.getYValue().doubleValue() / 3600000);
                    }
                }
            }
        });
      
        ChoiceBox<String> countryChoice = new ChoiceBox<>();
        // create a new list with all countries and add "All countries" as the first option
        ArrayList<String> countriesList = new ArrayList<>(countryEnergy.keySet());
        countriesList.add(0, "All countries");
        countryChoice.setItems(FXCollections.observableArrayList(countriesList));
      
        // select the "All countries" option as the default
        countryChoice.getSelectionModel().selectFirst();
      
        countryChoice.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
            ArrayList<BarChart.Data> data = new ArrayList<BarChart.Data>();
            joulesCheckbox.setSelected(false);
            // Clear the chart's data
            chart.getData().clear();
            if (newVal.equals("All countries")) {
                // Add all the country data to the chart
                for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
                    String country = set.getKey();
                    ArrayList<Energy> items = set.getValue();
      
                    for (Energy item : items) {
                        data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));
                    }
                    chart.getData().add(new BarChart.Series(country, FXCollections.observableArrayList(data)));
                    data.clear();
                }
            } 
            
            else {
                // Get the energy data for the selected country
                ArrayList<Energy> items = countryEnergy.get(newVal);
      
                // Add the year and energy data to the data arraylist
                for (Energy item : items) {
                    data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));
                }
      
                // Add the new data to the chart
                chart.getData().add(new BarChart.Series(newVal, FXCollections.observableArrayList(data)));
            }
        });

        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab();
        tab1.setText("Bar Chart");
        tab1.setClosable(false);
        hbox.getChildren().addAll(joulesCheckbox, countryChoice);
        vbox.getChildren().addAll(menuBar, hbox, chart);
        tab1.setContent(vbox);
        tabPane.getTabs().add(tab1);
      
        // Creating tab 2 for line chart
        Tab tab2 = new Tab();
        tab2.setText("Tab 2");
        tab2.setClosable(false);

        // Copies contents of the set to array
        System.arraycopy(years.toArray(), 0, arrYears, 0, n);

        xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(arrYears));
        yAxis = new NumberAxis("kWh / Person", 0.0d, 130000.0d, 5000.0d);
        xAxis.setLabel("Years (2000 - 2021)");

        ArrayList<XYChart.Series> seriesOne = new ArrayList<XYChart.Series>();

        for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
            String country = set.getKey();

            // Converts values from set to arraylist
            ArrayList<Energy> items = set.getValue();
            ArrayList<XYChart.Data> data = new ArrayList<XYChart.Data>();

            // adds year and energy to data array
            for (Energy item : items) {
                data.add(new XYChart.Data(item.getIntYear(), item.getDblkWh()));
            }
            // adds line chart data from data arraylist
            seriesOne.add(new XYChart.Series(country, FXCollections.observableArrayList(data)));
        }

        ObservableList<XYChart.Series> lineChartData = FXCollections.observableArrayList(seriesOne);

        // Line chart code
        final LineChart chart = new LineChart(xAxis, yAxis, lineChartData);

        VBox vboxOne = new VBox();
        HBox hboxOne = new HBox();

        Label titleTwo = new Label("Energy Consumption Line Graph");
        titleTwo.setStyle("-fx-font-size: 30px;");
        titleTwo.setPadding(new Insets(0,0,-90, 460));
        vboxOne.getChildren().add(0, titleTwo);

        vboxOne.setMaxSize(1280, 720);
        chart.setMaxSize(1280, 720);

        hboxOne.setPadding(new Insets(10, 10, 10, 10));
        vboxOne.setSpacing(10);
        hboxOne.setAlignment(Pos.CENTER);
        vboxOne.getChildren().add(chart);

        tab2.setContent(vboxOne);
        tabPane.getTabs().add(tab2);
      
        // Data table code
        Tab tab3 = new Tab();
        tab3.setText("Data Table");
        tab3.setClosable(false);
        
        TableView<Energy> table = new TableView<Energy>();
        ObservableList<Energy> data = FXCollections.observableArrayList();
        
        for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
          ArrayList<Energy> items = set.getValue();
          data.addAll(items);
        }
        
        TableColumn<Energy, String> countryCol = new TableColumn<Energy, String>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory<Energy, String>("strCountry"));
        TableColumn<Energy, Integer> yearCol = new TableColumn<Energy, Integer>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<Energy, Integer>("intYear"));
        TableColumn<Energy, Double> energyCol = new TableColumn<Energy, Double>("Energy (kWh/person)");
        energyCol.setCellValueFactory(new PropertyValueFactory<Energy, Double>("dblkWh"));
        
        table.setItems(data);
        table.getColumns().addAll(yearCol, countryCol, energyCol);
      
        VBox dataTableContainer = new VBox();
        dataTableContainer.setAlignment(Pos.CENTER);
        dataTableContainer.getChildren().add(table);
        tab3.setContent(dataTableContainer);
        dataTableContainer.setPadding(new Insets(10));
      
        tabPane.getTabs().add(tab3);
      
        return tabPane;
      }
}