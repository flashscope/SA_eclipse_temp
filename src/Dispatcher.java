import java.net.ServerSocket;

public interface Dispatcher {
	public void startDispatching(ServerSocket serverSocket, HandleMap handlers);
}
