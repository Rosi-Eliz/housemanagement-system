package Aufgabe1PLC21WS;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

public abstract class Wohnung implements Serializable
{
    class Adresse implements Serializable{
        public Integer PLZ;
        public String strasse;
        public Integer hausnummer;
        public Integer top;

        public Adresse(Integer PLZ, String strasse, Integer hausnummer, Integer top) {
            this.PLZ = PLZ;
            this.strasse = strasse;
            this.hausnummer = hausnummer;
            this.top = top;
        }

        @Override
        public String toString() {
            return
                    "PLZ:           " + PLZ +
                    "\nStrasse:            " + strasse +
                    "\nHausnummer:         " + hausnummer +
                    "\nTop:            " + top;
        }

    };

    private Integer id; //(Immobiliennummer - Zahl, eindeutig, aber nicht notwendigerweise fortlaufend)
    private Double flaeche;
    private Integer zimmeranzahl;
    private Integer stockwerk; //(0 = Erdgeschoß)
    private Integer baujahr;
    private Adresse adresse; //(PLZ, Strasse, Hausnummer, Top)

    public Wohnung(Integer id, Double flaeche, Integer zimmeranzahl, Integer stockwerk, Integer baujahr, Integer PLZ, String strasse, Integer hausnummer, Integer top) {
        if(baujahr < 0 ||baujahr > 2021 || stockwerk < 0 || zimmeranzahl < 0 || flaeche < 0 || hausnummer < 0 || top < 0)
            throw new IllegalArgumentException("Error: Parameter ungueltig");

        this.id = id;
        this.flaeche = flaeche;
        this.zimmeranzahl = zimmeranzahl;
        this.stockwerk = stockwerk;
        this.baujahr = baujahr;
        this.adresse = new Adresse(PLZ, strasse, hausnummer, top);
    }

    Integer alter()
    {
        return 2021 - baujahr;
    }

    abstract Double gesamtKosten();

    public Integer getId() {
        return id;
    }

    public Double getFlaeche() {
        return flaeche;
    }

    public Integer getZimmeranzahl() {
        return zimmeranzahl;
    }

    public Integer getStockwerk() {
        return stockwerk;
    }

    public Integer getBaujahr() {
        return baujahr;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setFlaeche(Double flaeche) {
        this.flaeche = flaeche;
    }

    public void setZimmeranzahl(Integer zimmeranzahl) {
        this.zimmeranzahl = zimmeranzahl;
    }

    public void setStockwerk(Integer stockwerk) {
        this.stockwerk = stockwerk;
    }

    public void setBaujahr(Integer baujahr) {
        this.baujahr = baujahr;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    //get Adresse methods:
    public Integer getPLZ() {
        return adresse.PLZ;
    }

    public String getStrasse() {
        return adresse.strasse;
    }

    public Integer getHausnummer() {
        return adresse.hausnummer;
    }

    public Integer getTop() {
        return adresse.top;
    }

    @Override
    public String toString() {
        String type = this instanceof MietWohnung ? "MW" : "EW";
        DecimalFormat df = Wohnung.getDecimalFormat();
        String flaecheFormatted = df.format(flaeche);
        return  "Typ:                   " + type +
                "\nId:                  " + id +
                "\nFlaeche:             " + flaecheFormatted +
                "\nZimmeranzahl:        " + zimmeranzahl +
                "\nStockwerk:           " + stockwerk +
                "\nBaujahr:             " + baujahr +
                "\n" + adresse.toString() +
                '\n';
    }

    public static DecimalFormat getDecimalFormat()
    {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        return new DecimalFormat("0.00", dfs);
    }

    @Override
    public boolean equals(Object wohnung) {
        if (wohnung == this)
            return true;

        if (!(wohnung instanceof Wohnung))
            return false;

        return this.id.equals(((Wohnung)wohnung).getId());
    }
}
