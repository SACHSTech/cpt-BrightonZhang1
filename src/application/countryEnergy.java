package application;

public class countryEnergy {
    private String strCountry;
    private int intYear;
    private double dblkWh;

    public countryEnergy(String country, int year, double kWh){
        strCountry = country;
        intYear = year;
        dblkWh = kWh;
    }

    public String getStrCountry() {
        return strCountry;
    }

    public void setStrCountry(String strCountry) {
        this.strCountry = strCountry;
    }

    public int getIntYear() {
        return intYear;
    }

    public void setIntYear(int intYear) {
        this.intYear = intYear;
    }

    public double getDblkWh() {
        return dblkWh;
    }

    public void setDblkWh(double dblkWh) {
        this.dblkWh = dblkWh;
    }
}
