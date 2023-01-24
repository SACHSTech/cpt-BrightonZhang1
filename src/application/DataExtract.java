package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataExtract {
    public static String comma = ",";
    ArrayList<String> yearList = new ArrayList<String>();
    HashMap<String, ArrayList<Energy>> countryEnergy = new HashMap<String, ArrayList<Energy>>();

    public ArrayList<String> getYearList() {
        return yearList;
    }

    public void setYearList(ArrayList<String> yearSet) {
        this.yearList = yearSet;
    }

    public HashMap<String, ArrayList<Energy>> getcountryEnergy() {
        return countryEnergy;
    }

    public void setcountryEnergy(HashMap<String, ArrayList<Energy>> countryEnergy) {
        this.countryEnergy = countryEnergy;
    }

    public void selectionSort(ArrayList <String> array) {
        for (int sorted = 0; sorted < array.size() - 1; sorted++){
            int smallestIndex = sorted;

            for(int check = sorted + 1; check < array.size(); check++)
                if(array.get(check).compareTo(array.get(smallestIndex)) < 0)
                    smallestIndex = check;

            String temp = array.get(smallestIndex);
            array.set(smallestIndex, array.get(sorted));
            array.set(sorted, temp);
        }
    }

    public ArrayList<String> getUniqueCountries() {
        Set<String> countrySet = countryEnergy.keySet();
        ArrayList<String> countryList = new ArrayList<String>(countrySet);

        // merge sort HERE
        selectionSort(countryList);
        return countryList;
    }
 
    public void csvConvert() throws IOException {
        Set<String> yearSet = new HashSet<String>();
        String country = "";
        int fileColCounter = 0;
        File file = new File("C:/Users/brigh/github-classroom/SACHSTech/cpt-BrightonZhang1/src/energy.csv");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = " ";
        String[] tempArray;
        
        while ((line = br.readLine()) != null) {
            if (fileColCounter == 0) {
                fileColCounter++;
                continue;
            }

            tempArray = line.split(comma);
            country = tempArray[0];

            ArrayList<Energy> things = countryEnergy.get(country);
            if (things == null) {
                things = new ArrayList<Energy>();
            }

            things.add(new Energy(country, tempArray[1], Double.parseDouble(tempArray[2])));
            countryEnergy.put(country, things);

            yearSet.add(tempArray[1]);
        }
        br.close();
        yearList = new ArrayList<String>(yearSet);

        // SORT HERE
        selectionSort(yearList);
    }
} 
