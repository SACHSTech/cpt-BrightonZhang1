package application;

public class Energy {
    private String strCountry;
    private String intYear;
    private double dblkWh;

    public Energy(String country, String year, double kWh){
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

    public String getIntYear() {
        return intYear;
    }

    public void setIntYear(String intYear) {
        this.intYear = intYear;
    }

    public double getDblkWh() {
        return dblkWh;
    }

    public void setDblkWh(double dblkWh) {
        this.dblkWh = dblkWh;
    }
}
