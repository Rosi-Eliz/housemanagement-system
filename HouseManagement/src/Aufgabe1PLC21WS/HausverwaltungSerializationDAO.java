package Aufgabe1PLC21WS;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class HausverwaltungSerializationDAO implements HausverwaltungDAO {

    private String fileName;
    private List<Wohnung> wohnungenList;

    public HausverwaltungSerializationDAO(String fileName) {
        this.fileName = fileName;
        wohnungenList = getWohnungen();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Wohnung> getWohnungen() {
        List<Wohnung> wohnungen = new ArrayList<>();

        File file = new File(fileName);
        if(!file.exists())
            return wohnungen;

        try (InputStream inputStream = new FileInputStream(fileName);
            ObjectInputStream reader = new ObjectInputStream(inputStream)) {
            Object object = reader.readObject();
            if(object != null)
                wohnungen.addAll((ArrayList<Wohnung>) object);
        } catch (Exception e) {
            System.err.println("Fehler bei Deserialisierung: " +
                    e.getMessage());
            System.exit(1);
        }
        return wohnungen;
    }

    @Override
    public Wohnung getWohnungbyId(int id) {

        Wohnung wohnung = getWohnungen()
                .stream()
                .filter(w -> w.getId().equals(id))
                .findFirst()
                .orElse(null);

        return wohnung;
    }

    @Override
    public void saveWohnung(Wohnung wohnung) {
        if(!wohnungenList.contains(wohnung))
        {
            wohnungenList.add(wohnung);
            try {
                writeObjectsToFile(wohnungenList);
            } catch(IOException e) {
                wohnungenList.remove(wohnung);
                System.err.println(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Error: Wohnung bereits vorhanden. (id= " + wohnung.getId() + ")");
        }
    }

    private void writeObjectsToFile(List<Wohnung> wohnungen) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(wohnungen);
            objectOutputStream.flush();
        }  catch (Exception e) {
                System.err.println("Fehler bei Serialisierung: " +
                        e.getMessage());
                System.exit(1);
            }
    }

    @Override
    public void deleteWohnung(int id) {
        Wohnung wohnung = getWohnungbyId(id);
        if(wohnung != null){
                try {
                    wohnungenList.remove(wohnung);
                    writeObjectsToFile(wohnungenList);
                } catch (Exception e) {
                    System.err.println("Fehler bei Serialisierung: " +
                            e.getMessage());
                    wohnungenList.add(wohnung);
                    System.exit(1);
                }
            } else {
            throw new IllegalArgumentException("Error: Wohnung nicht vorhanden. (id=" + id + ")");
        }
    }
}