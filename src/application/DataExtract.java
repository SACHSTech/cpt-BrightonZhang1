package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataExtract {
    public static final String delimiter = ",";
    ArrayList<String> yearList = new ArrayList<String>();
    HashMap<String, ArrayList<Energy>> countrySpaceItems = new HashMap<String, ArrayList<SpaceItem>>();

    public ArrayList<String> getYearList() {
        return yearList;
    }

    public void setYearList(ArrayList<String> yearSet) {
        this.yearList = yearSet;
    }

    public HashMap<String, ArrayList<SpaceItem>> getCountrySpaceItems() {
        return countrySpaceItems;
    }

    public void setCountrySpaceItems(HashMap<String, ArrayList<SpaceItem>> countrySpaceItems) {
        this.countrySpaceItems = countrySpaceItems;
    }

    public ArrayList<String> getUniqueCountries() {
        Set<String> countrySet = countrySpaceItems.keySet();
        ArrayList<String> countryList = new ArrayList<String>(countrySet);
        Collections.sort(countryList);
        return countryList;
    }

    public void csvConvert() throws IOException {
        Set<String> yearSet = new HashSet<String>();
        String country = "";
        int fileColCounter = 0;
        File file = new File("C:/Users/buyat/Downloads/objectsInSpace.csv");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = " ";
        String[] tempArr;
        while ((line = br.readLine()) != null) {
            if (fileColCounter == 0) {
                fileColCounter++;
                continue;
            }
            tempArr = line.split(delimiter);
            country = tempArr[0];

            ArrayList<SpaceItem> things = countrySpaceItems.get(country);
            if (things == null) {
                things = new ArrayList<SpaceItem>();
            }
            // ArrayList<SpaceItem> createThing = new ArrayList<SpaceItem>();
            things.add(new SpaceItem(country, tempArr[1], tempArr[2], Integer.parseInt(tempArr[3])));
            countrySpaceItems.put(country, things);

            yearSet.add(tempArr[2]);
        }
        br.close();
        yearList = new ArrayList<String>(yearSet);
        Collections.sort(yearList);
    }
}
