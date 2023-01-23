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
    public static final String comma = ",";
    ArrayList<String> yearList = new ArrayList<String>();
    HashMap<String, ArrayList<countryEnergy>> countryEnergy = new HashMap<String, ArrayList<countryEnergy>>();

    public ArrayList<String> getYearList() {
        return yearList;
    }

    public void setYearList(ArrayList<String> yearSet) {
        this.yearList = yearSet;
    }

    public HashMap<String, ArrayList<countryEnergy>> getcountryEnergy() {
        return countryEnergy;
    }

    public void setcountryEnergy(HashMap<String, ArrayList<countryEnergy>> countryEnergy) {
        this.countryEnergy = countryEnergy;
    }

    public ArrayList<String> getUniqueCountries() {
        Set<String> countrySet = countryEnergy.keySet();
        ArrayList<String> countryList = new ArrayList<String>(countrySet);
        Collections.sort(countryList);
        return countryList;
    }

    public void csvConvert() throws IOException {
        Set<String> yearSet = new HashSet<String>();
        String country = "";
        int fileColCounter = 0;
        File file = new File("C:/Users/brigh/Downloads/energy.csv");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = " ";
        String[] tempArr;
        while ((line = br.readLine()) != null) {
            if (fileColCounter == 0) {
                fileColCounter++;
                continue;
            }
            tempArr = line.split(comma);
            country = tempArr[0];

            ArrayList<countryEnergy> things = countryEnergy.get(country);
            if (things == null) {
                things = new ArrayList<countryEnergy>();
            }

            things.add(new countryEnergy(country, tempArr[1], Double.parseDouble(tempArr[2])));
            countryEnergy.put(country, things);

            yearSet.add(tempArr[2]);
        }
        br.close();
        yearList = new ArrayList<String>(yearSet);
        Collections.sort(yearList);
    }
} 
