package Aufgabe1PLC21WS;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class HausverwaltungSerializationDAO implements HausverwaltungDAO {

    private String fileName;

    public HausverwaltungSerializationDAO(String fileName) {
        this.fileName = fileName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Wohnung> getWohnungen() {
        List<Wohnung> wohnungen = new ArrayList<>();
        try {
            ObjectInputStream reader;
            File file = new File(fileName);
            if(!file.exists())
                return wohnungen;
            reader = new ObjectInputStream(new FileInputStream(fileName));
            Wohnung object = (Wohnung) reader.readObject();
            if(object != null)
                wohnungen.add(object);
            else
                return wohnungen;
            //wohnungen = (List<Wohnung>) reader.readObject(); // unchecked
            reader.close();
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
        if (!getWohnungen().contains(wohnung)) {
            File file = new File(fileName);
            try {
                ObjectOutputStream writer = new ObjectOutputStream(new
                        FileOutputStream(fileName, true));
                writer.writeObject(wohnung);
                writer.close();
            } catch (Exception e) {
                System.err.println("Fehler bei Serialisierung: " +
                        e.getMessage());
                System.exit(1);
            }
        } else {
           throw new IllegalArgumentException("Error: Wohnung bereits vorhanden. (id= " + wohnung.getId() + ")");
        }
    }

    private void writeObjectsToFile(List<Wohnung> wohnungen) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(wohnungen);
            objectOutputStream.flush();
        }
    }

    @Override
    public void deleteWohnung(int id) {
        List<Wohnung> wohnungen = getWohnungen();
        for (Wohnung wohnung : wohnungen) {
            if (wohnung.getId().equals(id)) {
                wohnungen.remove(wohnung);
                try {
                    writeObjectsToFile(wohnungen);
                } catch (Exception e) {
                    System.err.println("Fehler bei Serialisierung: " +
                            e.getMessage());
                    System.exit(1);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Error: Wohnung nicht vorhanden. (id= " + id + ")");
    }
}