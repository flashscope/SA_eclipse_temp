import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class ServerInitializer {

	private static final int PORT = 5000;
	private static final int HEADER_SIZE = 6;
	
	private static HandleMap handlers;

	private int threadPoolSize = 8;
	private int initialSize = 4;
	private int backlog = 50;
	
	public void start() throws IOException {
		
		handlers = new HandleMap();
		ArrayList<String> handlerList = getHandlerList();

		
		for (int i = 0; i < handlerList.size(); ++i) {
			try {
				EventHandler handler = (EventHandler) Class.forName(handlerList.get(i)).newInstance();
				handlers.put(handler.getHandle(), handler);
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		
		System.out.println("SERVER START!");
		
		ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
		AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(executor, initialSize);
		AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(group);
		listener.bind(new InetSocketAddress(PORT), backlog);
		listener.accept(listener, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {

			@Override
			public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel listener) {
				listener.accept(listener, this);
				
				ByteBuffer buffer = ByteBuffer.allocateDirect(HEADER_SIZE);
				channel.read(buffer, buffer, new Demultiplexer(channel, buffer, handlers));
				
			}

			@Override
			public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {
				exc.printStackTrace();
				try {
					listener.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					System.exit(-1);
				}
			}
		});
	}

	public static void main(String[] args) throws IOException {
		ServerInitializer server = new ServerInitializer();
		server.start();
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