import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Michael J.Donahoo, Kenneth L.Calvert
 * @editor Dongkuk Kim, dongkuk5411@nhnnext.org
 * @version 0.0.1
 * @brief 디스패칭 전에 쓰레드를 미리 생성하는 디스패처 클래스
 * @details 클라이언트의 요청을 받기 전 미리 적절양의 쓰레드를 할당하여 이벤트를 처리하는 디스패처이다.
 * @date 2014-01-16
 */
public class ThreadPoolDispatcher implements Dispatcher {

    static final String NUMTHREADS = "8";
    static final String THREADPROP = "Threads";

    private int numThreads;

    /**
     * @brief 풀디스패처의 생성자
     * @details 시스템 환경설정에 Threads의 개수를 저장한다.
     */
    public ThreadPoolDispatcher() {
        numThreads = Integer.parseInt(System.getProperty(THREADPROP, NUMTHREADS));
    }

    /**
     * @param serverSocket     서버 소켓 클래스
     * @brief 디스패칭을 시작하는 메서드
     * @details 클라이언트의 요청을 받기전 적정 수의 쓰레드를 생성하는 메서드이다. 메인 쓰레드를 포함한 모든 쓰레드는 디스패치를 시작한다.
     */
	@Override
	public void startDispatching(final ServerSocket serverSocket, final HandleMap handlers) {
        for (int i = 0; i < (numThreads - 1); i++) {
            Thread thread = new Thread() {
                public void run() {
                    dispatchLoop(serverSocket, handlers);
                }
            };
            thread.start();
            System.out.println("Created and started Thread = " + thread.getName());
        }
        System.out.println("Iterative server starting in main thread " +
                Thread.currentThread().getName());
        
        dispatchLoop(serverSocket, handlers);
	}
	
	
    /**
     * @param servSock     서버 소켓 클래스
     * @param logger       로그 남기는 클래스
     * @param protoFactory 프로토콜 생성 클래스
     * @brief 디스패칭하는 메서드
     * @details 각 쓰레드 별로 클라이언트의 요청을 받으면 적절한 프로토콜 처리 클래스를 생성한 후 작업을 넘긴다.
     */
    private void dispatchLoop(ServerSocket serverSocket, HandleMap handlers) {
    	
        // Run forever, accepting and handling each connection
    	while( true ) {
    		
			try {
				Socket socket = serverSocket.accept();
				Runnable demultiplexer = new Demultiplexer(socket, handlers);
	        	demultiplexer.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
    }


}
