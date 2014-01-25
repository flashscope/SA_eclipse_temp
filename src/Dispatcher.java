

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @brief 이벤트를 디코딩하여 적절한 핸들러에 전달한다.
 * @details 이벤트가 발생하면 header를 읽어들여 적절한 Protocol을 찾아 실행시킨다.
 */
public class Dispatcher {
    private ServerSocket serverSocket;


    /**
     * @brief 생성자.
     * @details 서버 소켓을 생성한다.
     * @return Nothing
     * @throws IOException IOException 발생시 던진다.
     */
    public Dispatcher(int servPort) throws IOException {
        serverSocket = new ServerSocket(servPort);
    }
    
    public void startDispatch() throws IOException {
    	while (true) {
        	Socket socket = serverSocket.accept();
        	Runnable demultiplexer = new Demultiplexer(socket);
        	Thread thread = new Thread(demultiplexer);
        	thread.start();
    	}
    }

}

