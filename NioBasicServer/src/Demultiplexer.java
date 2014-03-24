import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Demultiplexer {
	private final int HEADER_SIZE = 6;
	private static final int READ_MESSAGE_WAIT_TIME = 15;
	
	private AsynchronousSocketChannel asynchronousSocketChannel;
	private HandleMap handlers;
	
	public Demultiplexer(AsynchronousSocketChannel asynchronousSocketChannel, HandleMap handlers) {
		this.asynchronousSocketChannel = asynchronousSocketChannel;
		this.handlers = handlers;
		
	}
	public void demultiplex() {
		
		try {
			ByteBuffer messageByteBuffer = ByteBuffer.allocate(HEADER_SIZE);
			Future<Integer> futureReadResult = asynchronousSocketChannel.read(messageByteBuffer);
			futureReadResult.get(READ_MESSAGE_WAIT_TIME, TimeUnit.SECONDS);
			
			// 받아온 데이터를 String으로 변환
			String header = new String(messageByteBuffer.array()).trim();  
			System.out.println("HEADER:" + header);
			
			handlers.get(header).handleEvent(asynchronousSocketChannel);
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
