package application;

/**
 * Energy class is for storing information on each data point
 * 
 * @author B. Zhang
 */

public class Energy {
    // instance variables
    private String strCountry;
    private String intYear;
    private double dblkWh;

    /**
     * Constructor
     * 
     * @param country country of data point
     * @param year year of data point
     * @param kWh amount of energy of data point
     */
    public Energy(String country, String year, double kWh){
        strCountry = country;
        intYear = year;
        dblkWh = kWh;
    }

    public String getStrCountry() {
        return strCountry;
    }

    public String getIntYear() {
        return intYear;
    }

    public double getDblkWh() {
        return dblkWh;
    }
}
