package Aufgabe1PLC21WS;

public class EigentumsWohnung extends Wohnung {
    private Integer betriebskosten;
    private Integer reparaturruecklage;

    public EigentumsWohnung(Integer id, Double flaeche, Integer zimmeranzahl, Integer stockwerk, Integer baujahr, Integer PLZ, String strasse, Integer hausnummer, Integer top, Integer betriebskosten, Integer reparaturruecklage) {
        super(id, flaeche, zimmeranzahl, stockwerk, baujahr, PLZ, strasse, hausnummer, top);
        this.betriebskosten = betriebskosten;
        this.reparaturruecklage = reparaturruecklage;
    }

    @Override
    Double gesamtKosten() {
        Double kosten = betriebskosten * getFlaeche() + reparaturruecklage * getFlaeche();
        Integer percentage = getStockwerk() * 2;
        return (100+percentage)/100 * kosten;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "Betriebskosten:            " + betriebskosten +
                "\n" +
                "Ruecklage:             " + reparaturruecklage;
    }
}