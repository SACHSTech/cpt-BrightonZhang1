package application;

/**
 * Charts class generates and designs graph layouts
 * 
 * @author B. Zhang
 */

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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Charts {
    
    // Instance variables
    private HashMap<String, ArrayList<Energy>> countryEnergy;
    private ArrayList<String> years;
    private BarChart chartOne;
    private LineChart chartTwo;
    private CategoryAxis xAxisBar;
    private NumberAxis yAxisBar;
    private CategoryAxis xAxisLine;
    private NumberAxis yAxisLine;

    /**
     * Charts constructor method
     * @param countryEnergy
     * @param years
     */
    public Charts(HashMap<String, ArrayList<Energy>> countryEnergy, ArrayList<String> years) {
        this.countryEnergy = countryEnergy;
        this.years = years;
    }

    /**
     * createContent() method creates layout of the program
     * 
     * @return tabPane
     */
    public Parent createContent() throws IOException {
        int n = years.size();
        String arrYears[] = new String[n];

        // Copies contents of the set to array
        System.arraycopy(years.toArray(), 0, arrYears, 0, n);

        // Bar chart series
        ArrayList<BarChart.Series> series = new ArrayList<BarChart.Series>();

        // Sets axis labels and range
        xAxisBar = new CategoryAxis();
        xAxisBar.setCategories(FXCollections.<String>observableArrayList(arrYears));
        yAxisBar = new NumberAxis("kWh / Person", 0.0d, 130000.0d, 5000.0d);
        xAxisBar.setLabel("Years (2000 - 2021)");

        // Loops through set
        for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
          String country = set.getKey();

          // Converts values from set to arraylist
          ArrayList<Energy> items = set.getValue();
          ArrayList<BarChart.Data> data = new ArrayList<BarChart.Data>();

          // Adds year and energy to data array
          for (Energy item : items) {
            data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));
      
          }
          // Adds barchart data from data arraylist
          series.add(new BarChart.Series(country, FXCollections.observableArrayList(data)));
      
        }
      
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(series);
        chartOne = new BarChart(xAxisBar, yAxisBar, barChartData, 25.0d);
        
        // Vbox and Hbox object creation
        VBox vbox = new VBox();
        HBox hbox = new HBox();
      
        // Adds title to tab1
        Label titleOne = new Label("Energy Consumption Bar Graph");
        titleOne.setStyle("-fx-font-size: 30px;");
        titleOne.setPadding(new Insets(0,0,-90, 460));
        vbox.getChildren().add(0, titleOne);
      
        vbox.setMaxSize(1280, 720);
        chartOne.setMaxSize(1280, 720);
      
        hbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        hbox.setSpacing(10);
        
        // Checkbox object creation
        CheckBox joulesCheckboxOne = new CheckBox("View in Joules");
        joulesCheckboxOne.setPadding(new Insets(0, 0, 0, 75));
        joulesCheckboxOne.setSelected(false);
      
        // Stores the original upper and lower bounds of the y-axis
        double originalUpperBound = yAxisBar.getUpperBound();
        double originalLowerBound = yAxisBar.getLowerBound();
      
        // Variable to store the original tick unit of the y-axis
        double originalTickUnit = yAxisBar.getTickUnit();
      
        // Listener updates newVal when user clicks button
        joulesCheckboxOne.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Converts the upper and lower bounds of the y-axis to match with joules proportionally
                yAxisBar.setUpperBound(originalUpperBound * 3600000);
                yAxisBar.setLowerBound(originalLowerBound * 3600000);
                yAxisBar.setLabel("Joules / Person");
      
                // Sets ticks for Joules
                yAxisBar.setTickUnit(originalTickUnit * 3600000);
                for (XYChart.Series<String, Number> s : (ObservableList<XYChart.Series<String, Number>>) chartOne.getData()) {
                    for (XYChart.Data<String, Number> d : s.getData()) {
                        d.setYValue(d.getYValue().doubleValue() * 3600000);
                    }
                }
            } 
            
            else {
                // Sets original ticks if checkbox is unchecked
                yAxisBar.setUpperBound(originalUpperBound);
                yAxisBar.setLowerBound(originalLowerBound);
                yAxisBar.setLabel("kWh / Person");
                yAxisBar.setTickUnit(originalTickUnit);
                for (XYChart.Series<String, Number> s : (ObservableList<XYChart.Series<String, Number>>) chartOne.getData()) {
                    for (XYChart.Data<String, Number> d : s.getData()) {
                        d.setYValue(d.getYValue().doubleValue() / 3600000);
                    }
                }
            }
        });
      
        // Choicebox object creation
        ChoiceBox<String> countryChoiceOne = new ChoiceBox<>();

        // Creates a new list with all countries and adds "All countries" as the first option
        ArrayList<String> countriesList = new ArrayList<>(countryEnergy.keySet());
        countriesList.add(0, "All countries");
        countryChoiceOne.setItems(FXCollections.observableArrayList(countriesList));
      
        // Selects the "All countries" option as the default
        countryChoiceOne.getSelectionModel().selectFirst();
      
        // Listener updates newVal and records user input
        countryChoiceOne.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
            ArrayList<BarChart.Data> data = new ArrayList<BarChart.Data>();
            joulesCheckboxOne.setSelected(false);

            // Clears the chart's data
            chartOne.getData().clear();
            if (newVal.equals("All countries")) {

                // Adds all the country data to the chart
                for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
                    String country = set.getKey();
                    ArrayList<Energy> items = set.getValue();
      
                    for (Energy item : items) {
                        data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));
                    }
                    chartOne.getData().add(new BarChart.Series(country, FXCollections.observableArrayList(data)));
                    data.clear();
                }
            } 
            
            else {
                // Gets the energy data for the selected country
                ArrayList<Energy> items = countryEnergy.get(newVal);
      
                // Adds the year and energy data to the data arraylist
                for (Energy item : items) {
                    data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));
                }
      
                // Adds the new data to the chart
                chartOne.getData().add(new BarChart.Series(newVal, FXCollections.observableArrayList(data)));
            }
        });

        // Creates TabPane object
        TabPane tabPane = new TabPane();

        // Sets tab1
        Tab tab1 = new Tab();
        tab1.setText("Bar Chart");
        tab1.setClosable(false);

        // Adds CheckBox and ChoiceBox to hbox
        hbox.getChildren().addAll(joulesCheckboxOne, countryChoiceOne);

        // Adds hbox and chart object to vbox
        vbox.getChildren().addAll(hbox, chartOne);
        tab1.setContent(vbox);
        tabPane.getTabs().add(tab1);
      
        // Creating tab 2 for line chart
        VBox vboxOne = new VBox();
        HBox hboxOne = new HBox();

        // Copies contents of the set to array
        System.arraycopy(years.toArray(), 0, arrYears, 0, n);

        // Creates second series for line chart
        ArrayList<XYChart.Series> seriesOne = new ArrayList<XYChart.Series>();

        // Variables and labels for new x and y axis for line chart
        xAxisLine = new CategoryAxis();
        xAxisLine.setCategories(FXCollections.<String>observableArrayList(arrYears));
        yAxisLine = new NumberAxis("kWh / Person", 0.0d, 130000.0d, 5000.0d);
        xAxisLine.setLabel("Years (2000 - 2021)");

        for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
            String country = set.getKey();

            // Converts values from set to arraylist
            ArrayList<Energy> items = set.getValue();
            ArrayList<XYChart.Data> data = new ArrayList<XYChart.Data>();

            // Adds year and energy to data array
            for (Energy item : items) {
                data.add(new XYChart.Data(item.getIntYear(), item.getDblkWh()));
            }
            // Adds line chart data from data arraylist
            seriesOne.add(new XYChart.Series(country, FXCollections.observableArrayList(data)));
        }

        ObservableList<XYChart.Series> lineChartData = FXCollections.observableArrayList(seriesOne);

        // Line chart object instantiation
        chartTwo = new LineChart(xAxisLine, yAxisLine, lineChartData);

        // Adds label to second tab
        Label titleTwo = new Label("Energy Consumption Line Graph");
        titleTwo.setStyle("-fx-font-size: 30px;");
        titleTwo.setPadding(new Insets(20, 0, 0, 460));

        // New checkbox for line chart on tab 2
        CheckBox joulesCheckboxTwo = new CheckBox("View in Joules");
        joulesCheckboxTwo.setPadding(new Insets(-960, 910, 0, 0));
        joulesCheckboxTwo.setSelected(false);

        // Joules checkbox for the line chart
        joulesCheckboxTwo.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Converts the upper and lower bounds of the y-axis
                yAxisLine.setUpperBound(originalUpperBound * 3600000);
                yAxisLine.setLowerBound(originalLowerBound * 3600000);
                yAxisLine.setLabel("Joules / Person");
      
                // Sets ticks for Joules
                yAxisLine.setTickUnit(originalTickUnit * 3600000);
                for (XYChart.Series<String, Number> s : (ObservableList<XYChart.Series<String, Number>>) chartTwo.getData()) {
                    for (XYChart.Data<String, Number> d : s.getData()) {
                        d.setYValue(d.getYValue().doubleValue() * 3600000);
                    }
                }
            } 
            
            else {
                // Sets ticks to original value if unchecked
                yAxisLine.setUpperBound(originalUpperBound);
                yAxisLine.setLowerBound(originalLowerBound);
                yAxisLine.setLabel("kWh / Person");
                yAxisLine.setTickUnit(originalTickUnit);
                for (XYChart.Series<String, Number> s : (ObservableList<XYChart.Series<String, Number>>) chartTwo.getData()) {
                    for (XYChart.Data<String, Number> d : s.getData()) {
                        d.setYValue(d.getYValue().doubleValue() / 3600000);
                    }
                }
            }
        });

        // Choice box for line chart
        ChoiceBox<String> countryChoiceTwo = new ChoiceBox<>();

        // Creates a new list with all countries and add "All countries" as the first option
        countryChoiceTwo.setTranslateX(-900);
        countryChoiceTwo.setTranslateY(-475);
        countryChoiceTwo.setItems(FXCollections.observableArrayList(countriesList));
      
        // Selects the "All countries" option as the default
        countryChoiceTwo.getSelectionModel().selectFirst();
      
        // Listener updates newVal and checks for user input
        countryChoiceTwo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
            ArrayList<BarChart.Data> data = new ArrayList<BarChart.Data>();
            joulesCheckboxTwo.setSelected(false);

            // Clears the chart's data
            chartTwo.getData().clear();
            if (newVal.equals("All countries")) {

                // Adds all the country data to the chart
                for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
                    String country = set.getKey();
                    ArrayList<Energy> items = set.getValue();
      
                    for (Energy item : items) {
                        data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));
                    }
                    chartTwo.getData().add(new BarChart.Series(country, FXCollections.observableArrayList(data)));
                    data.clear();
                }
            } 
            
            else {
                // Gets the energy data for the selected country
                ArrayList<Energy> items = countryEnergy.get(newVal);
      
                // Adds the year and energy data to the data arraylist
                for (Energy item : items) {
                    data.add(new BarChart.Data(item.getIntYear(), item.getDblkWh()));
                }
      
                // Adds the new data to the chart
                chartTwo.getData().add(new BarChart.Series(newVal, FXCollections.observableArrayList(data)));
            }
        });
        
        // Adds title to second vBox object
        vboxOne.getChildren().add(0, titleTwo);

        vboxOne.setMaxSize(1280, 720);
        chartTwo.setMaxSize(1280, 720);

        // Fixes spacing
        hboxOne.setPadding(new Insets(10, 10, 10, 10));
        hboxOne.getChildren().addAll(joulesCheckboxTwo, countryChoiceTwo);
        vboxOne.setSpacing(10);
        hboxOne.setAlignment(Pos.CENTER);
        vboxOne.getChildren().addAll(chartTwo, hboxOne);

        // Creates a second tab labelled "Line Chart"
        Tab tab2 = new Tab("Line Chart", vboxOne);
        tab2.setClosable(false);
      
        tabPane.getTabs().add(tab2);

        // Creates a third tab for data table
        Tab tab3 = new Tab();
        tab3.setText("Data Table");
        tab3.setClosable(false);
        
        // Creates a TableView object
        TableView<Energy> table = new TableView<Energy>();
        ObservableList<Energy> data = FXCollections.observableArrayList();
        
        // Loops through set
        for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
          ArrayList<Energy> items = set.getValue();

          // Adds set value to data object arraylist
          data.addAll(items);
        }
        
        // Sets values and labels for columns
        TableColumn<Energy, String> countryCol = new TableColumn<Energy, String>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory<Energy, String>("strCountry"));
        TableColumn<Energy, Integer> yearCol = new TableColumn<Energy, Integer>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<Energy, Integer>("intYear"));
        TableColumn<Energy, Double> energyCol = new TableColumn<Energy, Double>("Energy (kWh/person)");
        energyCol.setCellValueFactory(new PropertyValueFactory<Energy, Double>("dblkWh"));
        
        // Adds data to table
        table.setItems(data);
        table.getColumns().addAll(yearCol, countryCol, energyCol);
      
        // New vbox object creation
        VBox dataTableContainer = new VBox();
        Label titleThree = new Label("Data Table");
        titleThree.setStyle("-fx-font-size: 30px;");
        titleThree.setPadding(new Insets(-100, 0, 0, 0));
        
        // Adds title and table to th tab
        dataTableContainer.getChildren().add(0, titleThree);
        dataTableContainer.setAlignment(Pos.CENTER);
        dataTableContainer.getChildren().add(table);
        tab3.setContent(dataTableContainer);
        dataTableContainer.setPadding(new Insets(10));
      
        tabPane.getTabs().add(tab3);
      
        // 
        return tabPane;
    }
}