import java.io.IOException;
import java.net.ServerSocket;


public class Reactor {
    private HandleMap handlers;
    private ServerSocket serverSocket;
    
    
    public Reactor(int servPort) {
        handlers = new HandleMap();
        try {
			serverSocket = new ServerSocket(servPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void handleEvents() {
    	//Dispatcher dispatcher = new ThreadPerDispatcher();
    	Dispatcher dispatcher = new ThreadPoolDispatcher();
    	
    	dispatcher.startDispatching(serverSocket, handlers);
    }
    
    public void registerHandler(EventHandler handler) {
        handlers.put(handler.getHandle(), handler);
    }

    public void removeHandler(EventHandler handler) {
        handlers.remove(handler.getHandle());
    }
}
