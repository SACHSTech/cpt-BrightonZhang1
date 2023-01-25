package application;

/**
 * Energy class is for storing information on each data point
 * 
 * @author B. Zhang
 */

public class Energy {
    // Instance variables (encapsulation)
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

    /**
     * Getter method for the strCountry variable
     * @return strCountry
     */
    public String getStrCountry() {
        return strCountry;
    }

    /**
     * Getter method for the intYear variable
     * @return intYear
     */
    public String getIntYear() {
        return intYear;
    }

    /**
     * Getter method for the dblkWh variable
     * @return dblkWh
     */
    public double getDblkWh() {
        return dblkWh;
    }
}
