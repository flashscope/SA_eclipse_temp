import java.nio.channels.AsynchronousSocketChannel;

public interface Dispatcher {
	public void startDispatching(AsynchronousSocketChannel asynchronousSocketChannel, HandleMap handlers);
}
