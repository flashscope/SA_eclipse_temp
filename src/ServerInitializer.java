

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;


/**
 * @author Michael J.Donahoo, Kenneth L.Calvert
 * @editor Dongkuk Kim, dongkuk5411@nhnnext.org
 * @version 0.0.1
 * @brief 서버가 시작하는 클래스
 * @details 소켓을 디스패처에 전달한다.
 * @date 2014-01-16
 */
public class ServerInitializer {

    public static void main(String[] args) throws Exception {

        int servPort = 5000;

        ServerInitializer serverInitializer = new ServerInitializer();
        serverInitializer.initializeServer(servPort);
    }

    public void initializeServer(int servPort) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    	System.out.println("Server ON");
    	
    	ArrayList<String> handlerList = getHandlerList();
    	
    	Reactor reactor = new Reactor(servPort);
    	
        for (int i = 0; i < handlerList.size(); i++) {
        	reactor.registerHandler( (EventHandler) Class.forName(handlerList.get(i)).newInstance());
		}

        reactor.handle_events();
    }
    
	private ArrayList<String> getHandlerList() {
		ArrayList<String> handlerList = new ArrayList<String>();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream("handlerList.xml");
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
