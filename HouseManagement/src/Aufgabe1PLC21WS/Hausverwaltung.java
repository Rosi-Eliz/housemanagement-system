package Aufgabe1PLC21WS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Hausverwaltung {

	private HausverwaltungDAO hausverwaltungDAO;

    public Hausverwaltung(HausverwaltungDAO hausverwaltungDAO) {
        this.hausverwaltungDAO = hausverwaltungDAO;
    }

    public String allWohnungenDetails(){
        List<Wohnung> wohnungen = hausverwaltungDAO.getWohnungen();

        if(wohnungen.isEmpty())
            return "Keine Wohnungen vorhanden";

        StringBuffer buffer = new StringBuffer();
        wohnungen.forEach(wohnung -> {
            buffer.append(wohnung.toString());
            buffer.append("\n");
        });
        return buffer.toString();
    };

    public String wohnungDetails(Integer id){
        Wohnung wohnung = hausverwaltungDAO.getWohnungbyId(id);
        if(wohnung != null)
            return wohnung.toString();

        return "Wohnung existiert nicht.";
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

    public Double meanTotalCosts(){
        Integer wohnungenTotal = getTotalAmountWohnungen();
        if(wohnungenTotal > 0) {
            Double totalCosts = hausverwaltungDAO.getWohnungen()
                    .stream()
                    .reduce(0.0, (subtotal, wohnung) -> subtotal + wohnung.gesamtKosten(),
                            (accumulatedDouble, costs) -> accumulatedDouble + costs);

            return totalCosts / wohnungenTotal;
        }
        return 0.0;
    }

    public String oldestWohnungenIDs(){
        List<Wohnung> wohnungen = new ArrayList<>(hausverwaltungDAO.getWohnungen());

        if(getTotalAmountWohnungen() == 0)
            return new String("Keine Wohnungen vorhanden");

        wohnungen.sort(Comparator.comparingInt(Wohnung::alter));
        Integer alter = wohnungen.get(wohnungen.size() - 1).alter();
        List<Integer> ids = wohnungen.stream().filter((Wohnung w) -> w.alter().equals(alter))
                .map(Wohnung::getId)
                .collect(Collectors.toList());

        StringBuffer buffer = new StringBuffer();
        for(Integer id : ids)
        {
            buffer.append("Id: ");
            buffer.append(id.toString());
            buffer.append("\n");
        }
        return buffer.toString();
    }
}