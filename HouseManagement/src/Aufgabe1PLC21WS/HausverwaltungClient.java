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
					assert (args.size() == 12);
					hausverwaltung.addWohnung(args);
					break;
				case "delete":
					assert (args.size() == 1);
					hausverwaltung.removeWohnung(Integer.parseInt(args.get(0)));
					break;
				case "count":
					assert (args.size() < 2);
					if(args.size() == 1)
					{
						switch(args.get(0))
						{
							case "MW" : hausverwaltung.getTotalAmountWohnungenMW(); break;
							case "EW" : hausverwaltung.getTotalAmountWohnungenEW(); break;
							default:
								throw new UnsupportedOperationException("Does not support this operation!");
						}
					} else {
						System.out.println(hausverwaltung.getTotalAmountWohnungen());
					}
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
		//String[] args = {"test.csv", "add", "EW", "1", "95", "3", "4", "1898", "1080", "Florianigasse", "42", "20", "1.55", "0.45"};


//		for(int i = 0; i < 2; i++)
//		{
			LinkedList<String> arguments = new LinkedList<String>(Arrays.asList(args));
			String fileName = arguments.get(0);
			String command = arguments.get(1);
			arguments.subList(0, 2).clear();
			HausverwaltungClient client = new HausverwaltungClient(fileName);
			client.executeOperation(command, arguments);
//		}

	}
	
}