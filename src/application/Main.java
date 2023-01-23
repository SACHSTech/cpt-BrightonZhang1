package application;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException{
        DataExtract e = new DataExtract();
        e.csvConvert();
        ArrayList <String> countries = new ArrayList<>();
        countries = e.getUniqueCountries();

        for(int i = 0; i < 10; i++){
        System.out.println(countries.get(i));
        }
    }
}
