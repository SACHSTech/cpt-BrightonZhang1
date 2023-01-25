package application;

/**
 * DataExtract class is for extracting data from the .csv file
 * 
 * @author B. Zhang
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataExtract {
    // Arraylist and hashmap used to store extracted data
    private static String comma = ",";
    ArrayList<String> yearList = new ArrayList<String>();
    HashMap<String, ArrayList<Energy>> countryEnergy = new HashMap<String, ArrayList<Energy>>();

    /**
     * Getter method for the yearList arraylist
     * @return yearList
     */
    public ArrayList<String> getYearList() {
        return yearList;
    }

    /**
     * Setter method for the yearList variable
     * @param yearSet
     */
    public void setYearList(ArrayList<String> yearSet) {
        this.yearList = yearSet;
    }

    /**
     * Getter method for the countryEnergy variable
     * @return getcountryEnergy
     */
    public HashMap<String, ArrayList<Energy>> getcountryEnergy() {
        return countryEnergy;
    }

    /**
     * Setter method for the countryEnergy variable
     * @param countryEnergy
     */
    public void setcountryEnergy(HashMap<String, ArrayList<Energy>> countryEnergy) {
        this.countryEnergy = countryEnergy;
    }

    /**
     * Selection sort algorithm that sorts alphabetically
     * @param array
     */
    public void selectionSort(ArrayList <String> array) {
        // Outer loop
        for (int sorted = 0; sorted < array.size() - 1; sorted++){
            int smallestIndex = sorted;

            // Inner loop
            for(int check = sorted + 1; check < array.size(); check++)

                // Checks if current element is alphabetically smaller than the smallest element
                if(array.get(check).compareTo(array.get(smallestIndex)) < 0)
                    smallestIndex = check;

            String temp = array.get(smallestIndex);
            array.set(smallestIndex, array.get(sorted));
            array.set(sorted, temp);
        }
    }

    /**
     * Getter method that converts country set to arraylist
     * @return countryList 
     */
    public ArrayList<String> getUniqueCountries() {
        Set<String> countrySet = countryEnergy.keySet();
        ArrayList<String> countryList = new ArrayList<String>(countrySet);
        return countryList;
    }

    /**
     * Method that collects data from .csv file
     */
    public void csvRead() throws IOException {
        // Variable instantiation
        Set<String> yearSet = new HashSet<String>();
        String country = "";

        // File Input / Output course connection
        int fileColCounter = 0;
        File file = new File("C:/Users/brigh/github-classroom/SACHSTech/cpt-BrightonZhang1/src/energy.csv");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = " ";
        String[] tempArray;

        // While loop checks whether the next line to be scanned exists
        while ((line = br.readLine()) != null) {
            if (fileColCounter == 0) {
                fileColCounter++;
                continue;
            }

            // Split used to separate information with comma
            tempArray = line.split(comma);
            country = tempArray[0];

            // Checks if country exists within list
            ArrayList<Energy> things = countryEnergy.get(country);
            if (things == null) {
                things = new ArrayList<Energy>();
            }

            // Inputs country name, year, and energy into object arraylist
            things.add(new Energy(country, tempArray[1], Double.parseDouble(tempArray[2])));
            countryEnergy.put(country, things);

            yearSet.add(tempArray[1]);
        }
        br.close();
        yearList = new ArrayList<String>(yearSet);

        // Sorts years in chart
        selectionSort(yearList);
    }

    /**
     * Getter method that takes all energy objects from hashmap returns and observable list
     * @return allEnergyData
     */
    public ObservableList<Energy> getAllEnergyData(){
        ArrayList<Energy> allEnergyData = new ArrayList<Energy>();
        for (Map.Entry<String, ArrayList<Energy>> set : countryEnergy.entrySet()) {
          allEnergyData.addAll(set.getValue());
        }
        return FXCollections.observableArrayList(allEnergyData);
    }
      
} 
