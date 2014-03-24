import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
//https://github.com/dublintech/async_nio2_java7_examples/blob/master/echo-nio2-server/src/main/java/com/alex/asyncexamples/server/AsyncEchoServer.java
//http://book.naver.com/bookdb/book_detail.nhn?bid=6950160    319p

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;


public class ServerInitializer {

    private static final int READ_MESSAGE_WAIT_TIME = 15;
    private static final int MESSAGE_INPUT_SIZE= 1024;
    
    private final int HEADER_SIZE = 6;
    private static HandleMap handlers;
    
    public static void main(String[] args) {

        final int DEFAULT_PORT = 5000;
        final String IP = "127.0.0.1";   
        
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
        
        // 기본 그룹에 바인딩된 비동기 서버 소켓 채널을 생성한다.
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {

        	// 비동기 서버 소켓 채널이 이미 열려 있는지 또는 성공적으로 채널을 열었는지 확인
            if (asynchronousServerSocketChannel.isOpen()) {

                // 몇 가지 옵션을 설정한다. (기본값을 사용 할 수 있으므로 필수는 아님)
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024); // 수신 버퍼의 크기 조정
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true); // 커널이 소켓의 포트를 아직 점유 중인 경우에도 그냥 bind 할수 있게 한다.
                
                /*
                 * 비동기 서버 소켓 채널에서 지원하는 옵션 보
                Set<SocketOption<?>> options = asynchronousServerSocketChannel.supportedOptions();
                for (SocketOption<?> option : options) {
                	System.out.println(option);
                }
                */
                
                // 비동기 서버 소켓 채널을 로컬 주소에 바인딩한다.
                asynchronousServerSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));

                // 클라이언트를 기다린다는 대기 메시지를 표시한다.
                System.out.println("Waiting for connections ...");
                while (true) {
                	// 새 연결을 수락 
                	Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel.accept();
                	
                	// 작업(연결 수락)이 성공적으로 완료되었을 때 새 연결을 반환
                    try (AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get()) {
                    	
                    	// 원격지의 주소 확인
                        System.out.println("Incoming connection from: " + asynchronousSocketChannel.getRemoteAddress());
                        
                        
                        Demultiplexer demultiplexer = new Demultiplexer(asynchronousSocketChannel, handlers);
                        demultiplexer.demultiplex();
                        /*
                        // 메시지를 받을 버퍼 생성
                        ByteBuffer messageByteBuffer = ByteBuffer.allocate(MESSAGE_INPUT_SIZE);
                        
                        // 데이터를 읽어 들인다.
                        Future<Integer> futureReadResult = asynchronousSocketChannel.read(messageByteBuffer);
            			futureReadResult.get(READ_MESSAGE_WAIT_TIME, TimeUnit.SECONDS);
            			
            			// 받아온 데이터를 String으로 변환
            			String clientMessage = new String(messageByteBuffer.array()).trim();  
            			System.out.println("READ:"+clientMessage);
            			
            			// 바이트 버퍼 초기화
            			messageByteBuffer.clear();
            			
            			// 바이트 버퍼의 읽기쓰기 모드를 전환
            			messageByteBuffer.flip();
            			
            			// 메시지 전송 (이 예제에서는 불필요)
            			String responseString = "echo" + "_" + clientMessage;
            			messageByteBuffer = ByteBuffer.wrap((responseString.getBytes()));
            			
            			// 바이트 버퍼 쓰기
            			Future<Integer> futureWriteResult = asynchronousSocketChannel.write(messageByteBuffer);
            			futureWriteResult.get(READ_MESSAGE_WAIT_TIME, TimeUnit.SECONDS);
            			
            			// 버퍼에 데이터가 남아있으면 남은 데이터를 버퍼의 앞으로 정렬 없으면 초기화
            			if (messageByteBuffer.hasRemaining()) {
            				messageByteBuffer.compact();
            			} else {
            				messageByteBuffer.clear();
            			}        
            			
                        System.out.println(asynchronousSocketChannel.getRemoteAddress() + " was successfully served!");
                        */
                        
                        
                    } catch (Exception ex) {
                    	asynchronousServerSocketChannel.close();
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
