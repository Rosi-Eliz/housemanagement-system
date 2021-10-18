package Aufgabe1PLC21WS;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Hausverwaltung {

	private HausverwaltungDAO hausverwaltungDAO;

    public Hausverwaltung(HausverwaltungDAO hausverwaltungDAO) {
        this.hausverwaltungDAO = hausverwaltungDAO;
    }

    public void allWohnungenDetails(){
        List<Wohnung> wohnungen = hausverwaltungDAO.getWohnungen();

        wohnungen.forEach(wohnung -> {
            System.out.println(wohnung.toString());
            System.out.println();
        });
    }

    public void wohnungDetails(Integer id){
        Wohnung wohnung = hausverwaltungDAO.getWohnungbyId(id);
        if(wohnung != null)
            System.out.print(wohnung.toString());
        else
            System.out.print("Wohnung existiert nicht.");
    }

    public void addWohnung(List<String> arguments){
        if(arguments.size() != 12)
            throw new IllegalArgumentException("Wrong number of Wohnung parameters!");

        Integer id = Integer.parseInt(arguments.get(1));
        Double  flaeche = Double.parseDouble(arguments.get(2));
        Integer zimmeranzahl = Integer.parseInt(arguments.get(3));
        Integer stockwerk = Integer.parseInt(arguments.get(4));
        Integer baujahr = Integer.parseInt(arguments.get(5));
        Integer PLZ = Integer.parseInt(arguments.get(6));
        String  strasse = arguments.get(7);
        Integer hausnummer = Integer.parseInt(arguments.get(8));
        Integer top = Integer.parseInt(arguments.get(9));

        Double x = Double.parseDouble(arguments.get(10));
        Double y = Double.parseDouble(arguments.get(11));
        Wohnung wohnung = null;

        if(arguments.get(0).equals("MW")) {
            wohnung = new MietWohnung(id, flaeche, zimmeranzahl, stockwerk, baujahr, PLZ, strasse, hausnummer, top, x, y);
        } else {
            wohnung = new EigentumsWohnung(id, flaeche, zimmeranzahl, stockwerk, baujahr, PLZ, strasse, hausnummer, top, x, y);
        }
        hausverwaltungDAO.saveWohnung(wohnung);
        System.out.println("Info: Wohnung " + wohnung.getId() + " added.");
    };

	public void removeWohnung(Integer id){
        try {
            hausverwaltungDAO.deleteWohnung(id);
        }catch(Exception e){
            System.out.println("Wohnung existiert nicht");
        }
        System.out.println("Info: Wohnung " + id + " deleted.");
    };

    public Integer getTotalAmountWohnungen(){
        return hausverwaltungDAO.getWohnungen().size();
    }

    public void getTotalAmountWohnungenEW(){
        System.out.println(hausverwaltungDAO.getWohnungen()
                .stream()
                .filter(wohnung -> wohnung instanceof EigentumsWohnung)
                .count());
    }

    public void getTotalAmountWohnungenMW(){
        System.out.println(hausverwaltungDAO.getWohnungen()
                .stream()
                .filter(wohnung -> wohnung instanceof MietWohnung)
                .count());
    }

    public static DecimalFormat getDecimalFormat()
    {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        return new DecimalFormat("0.00", dfs);
    }

    public void meanTotalCosts(){
        Integer wohnungenTotal = getTotalAmountWohnungen();
        if(wohnungenTotal > 0) {
            Double totalCosts = hausverwaltungDAO.getWohnungen()
                    .stream()
                    .reduce(0.0, (subtotal, wohnung) -> subtotal + wohnung.gesamtKosten(),
                            (accumulatedDouble, costs) -> accumulatedDouble + costs);

            DecimalFormat df = Hausverwaltung.getDecimalFormat();
            String costsFormatted = df.format(totalCosts / wohnungenTotal);
            System.out.println(costsFormatted);
        } else {
            System.out.println(0);
        }
    }

    public void oldestWohnungenIDs(){
        List<Wohnung> wohnungen = new ArrayList<>(hausverwaltungDAO.getWohnungen());

        if(getTotalAmountWohnungen().equals(0)) {
            System.out.print("Keine Wohnungen vorhanden");
            return;
        }
        wohnungen.sort(Comparator.comparingInt(Wohnung::alter));
        Integer alter = wohnungen.get(wohnungen.size() - 1).alter();
        List<Integer> ids = wohnungen.stream().filter((Wohnung w) -> w.alter().equals(alter))
                .map(Wohnung::getId)
                .collect(Collectors.toList());

        for(Integer id : ids)
        {
            System.out.println("Id: " + id.toString());
        }
    }
}