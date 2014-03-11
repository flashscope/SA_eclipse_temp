import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;



public class ServerInitializer {

	public static void main(String[] args) {
		int servPort = 5000;
		System.out.println("Server ON :" + servPort);

		ArrayList<String> handlerList = getHandlerList();
		
		Reactor reactor = new Reactor(servPort);

		for (int i = 0; i < handlerList.size(); ++i) {
			try {
				reactor.registerHandler((EventHandler) Class.forName(handlerList.get(i)).newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		
		reactor.handleEvents();
		

	}
	
	private static ArrayList<String> getHandlerList() {
		ArrayList<String> handlerList = new ArrayList<String>();
		try {
			InputStream in = new FileInputStream("handlerList.xml");
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			XMLEvent event;
			String nodeName = "";
			while (eventReader.hasNext()) {
				event = eventReader.nextEvent();

				if (event.isStartElement()) {
					nodeName = event.asStartElement().getName().toString();
				} else if (nodeName.equals("handler") && event.isCharacters()) {
					handlerList.add(event.asCharacters().getData());
				}

				if (event.isEndElement()) {
					nodeName = "";
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return handlerList;

	}

}
