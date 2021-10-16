package Aufgabe1PLC21WS;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HausverwaltungClient {
	private Hausverwaltung hausverwaltung;

	public void executeOperation(String op, List<String> args)
	{
		try {
			switch (op) {
				case "list":
					hausverwaltung.allWohnungenDetails();
					break;
				case "add":
					assert (args.size() == 1);
					hausverwaltung.addWohnung(args);
					break;
				case "delete":
					assert (args.size() == 1);
					hausverwaltung.removeWohnung(Integer.parseInt(args.get(0)));
					break;
				case "count":
					assert (args.isEmpty());
					hausverwaltung.getTotalAmountWohnungen();
					break;
				case "meancosts":
					assert (args.isEmpty());
					hausverwaltung.meanTotalCosts();
					break;
				case "oldest":
					assert (args.isEmpty());
					hausverwaltung.oldestWohnungenIDs();
					break;
				default:
					throw new UnsupportedOperationException("Does not support this operation!");
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public HausverwaltungClient(String fileName) {
		this.hausverwaltung =  new Hausverwaltung(new HausverwaltungSerializationDAO(fileName));
	}

	public static void main(String[] args) {
		if(args.length < 2)
		{
			throw new IllegalArgumentException("Wrong number of arguments");
		}

		LinkedList<String> arguments = new LinkedList<String>(Arrays.asList(args));
		String fileName = arguments.get(0);
		String command = arguments.get(1);
		arguments.subList(0, 2).clear();
		HausverwaltungClient client = new HausverwaltungClient(fileName);
		client.executeOperation(command, arguments);
	}
	
}