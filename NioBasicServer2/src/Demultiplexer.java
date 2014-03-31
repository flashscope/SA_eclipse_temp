import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {
	private static Charset utf8 = Charset.forName("utf-8");
	private AsynchronousSocketChannel channel;
	private ByteBuffer buffer;
	private HandleMap handlers;
	
	public Demultiplexer(AsynchronousSocketChannel channel, ByteBuffer buffer, HandleMap handlers) {
		this.channel = channel;
		this.buffer = buffer;
		this.handlers = handlers;
	}

	@Override
	public void completed(Integer result, ByteBuffer buff) {
		if (result == -1) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (result > 0) {
			buffer.flip();
			String msg = utf8.decode(buffer).toString();
			System.out.println("HEADER: " + msg);
			
			
			EventHandler handler = handlers.get(msg);
			
			ByteBuffer buffer = ByteBuffer.allocateDirect(handler.getDataSize());
			
			handler.initialize(channel, buffer);
			channel.read(buffer, buffer, handler);
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
}