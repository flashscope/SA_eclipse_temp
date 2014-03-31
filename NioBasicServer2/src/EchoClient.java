import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class EchoClient {
	private static Charset utf8 = Charset.forName("utf-8");
	private int port = 5000;
	private String remoteHost = "127.0.0.1";
	private String[] message = {"aaa", "bbb"};
	private AsynchronousSocketChannel channel;

	private static int countNum = 1001;
	
	public static void main(String args[]) throws Exception {
		// 아직 NIO용 클라는 미완성
		//while (true) {
		/*
		for (int i = 0; i < countNum; i++) {
			EchoClient client = new EchoClient();
			client.connect();
			client.sendAndReceive();
			Thread.sleep(5);
		}
		*/
			
	}
	
	public void sendAndReceive() throws InterruptedException, ExecutionException {
		ByteBuffer buffer = ByteBuffer.allocate(512);
		for (String msg : message) {
			Future<Integer> w = channel.write(utf8.encode(msg));
			w.get();
		}
		
		channel.read(buffer, buffer, new EchoClientReceiverHandler(channel, buffer));
	}

	public void close() throws IOException {
		channel.shutdownInput();
		channel.shutdownOutput();
	}



	public void connect() throws IOException, InterruptedException, ExecutionException {
		channel = AsynchronousSocketChannel.open();
		Future<Void> r = channel.connect(new InetSocketAddress(this.remoteHost, this.port));
		r.get();
	}

}