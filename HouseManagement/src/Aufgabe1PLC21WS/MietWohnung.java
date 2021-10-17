package Aufgabe1PLC21WS;

public class MietWohnung extends Wohnung{
	private Double mietkosten;
	private Double mieter;

    public MietWohnung(Integer id, Double flaeche, Integer zimmeranzahl, Integer stockwerk, Integer baujahr, Integer PLZ, String strasse, Integer hausnummer, Integer top, Double mietkosten, Double mieter) {
        super(id, flaeche, zimmeranzahl, stockwerk, baujahr, PLZ, strasse, hausnummer, top);
        this.mietkosten = mietkosten;
        this.mieter = mieter;
    }

    @Override
    Double gesamtKosten() {
        Double kosten = mietkosten * getFlaeche();
        Double percentage = 0.0;
        if(mieter > 1)
        {
            percentage = Math.max(mieter * 2.5,10);
        }
        return (100+percentage)/100 * kosten;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "Miete/m2:       " + mietkosten +
                "\n" +
                "Anzahl Mieter:  " + mieter;
    }
}