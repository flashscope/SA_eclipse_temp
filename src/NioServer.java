import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
//https://github.com/dublintech/async_nio2_java7_examples/blob/master/echo-nio2-server/src/main/java/com/alex/asyncexamples/server/AsyncEchoServer.java
//http://book.naver.com/bookdb/book_detail.nhn?bid=6950160    319p


public class NioServer {

    private static final int READ_MESSAGE_WAIT_TIME = 15;
    private static final int MESSAGE_INPUT_SIZE= 1024;
    
    public static void main(String[] args) {

        final int DEFAULT_PORT = 5000;
        final String IP = "127.0.0.1";        
        
        //create asynchronous server-socket channel bound to the default group
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {

            if (asynchronousServerSocketChannel.isOpen()) {

                //set some options
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                //bind the server-socket channel to local address
                asynchronousServerSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));

                //display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");
                while (true) {// The accept method does not block it sets up the CompletionHandler callback and moves on.
                	Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel.accept();

                    try (AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get()) {

                        System.out.println("Incoming connection from: " + asynchronousSocketChannel.getRemoteAddress());

                        ByteBuffer messageByteBuffer = ByteBuffer.allocate(1024);
                        
                        
                        Future<Integer> futureReadResult = asynchronousSocketChannel.read(messageByteBuffer);
            			futureReadResult.get(READ_MESSAGE_WAIT_TIME, TimeUnit.SECONDS);

            			String clientMessage = new String(messageByteBuffer.array()).trim();  

            			messageByteBuffer.clear();
            			messageByteBuffer.flip();
            			
            			System.out.println("READ:"+clientMessage);
            			
            			String responseString = "echo" + "_" + clientMessage;
            			messageByteBuffer = ByteBuffer.wrap((responseString.getBytes()));
            			Future<Integer> futureWriteResult = asynchronousSocketChannel.write(messageByteBuffer);
            			futureWriteResult.get(READ_MESSAGE_WAIT_TIME, TimeUnit.SECONDS);
            			if (messageByteBuffer.hasRemaining()) {
            				messageByteBuffer.compact();
            			} else {
            				messageByteBuffer.clear();
            			}        
            			
            			
                        
                        System.out.println(asynchronousSocketChannel.getRemoteAddress() + " was successfully served!");

                    } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
                        System.err.println(ex);
                    }
                }
            } else {
                System.out.println("The asynchronous server-socket channel cannot be opened!");
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }

    }
    


}
