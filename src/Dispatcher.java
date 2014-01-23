

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @brief 이벤트를 디코딩하여 적절한 핸들러에 전달한다.
 * @details 이벤트가 발생하면 header와 data 부분으로 나눈다. 이중 header는 적절한 handler를 찾는데 쓰이고, data는 handler에 전달한다.
 */
public class Dispatcher {
    private ServerSocket serverSocket;
    private final int HEADER_SIZE = 6;

    /**
     * @brief 생성자.
     * @details 서버 소켓을 생성한다.
     * @return Nothing
     * @throws IOException IOException 발생시 던진다.
     */
    public Dispatcher(int servPort) throws IOException {
        serverSocket = new ServerSocket(servPort);
    }

    /**
     * @brief 이벤트 발생시 Demultiplex 한다.
     * @details 이벤트 발생시, header와 data로 나눈다. 이중 header는 적절한 handler를 찾는데 쓰이고, data는 handler에 전달한다.
     * @param handlers Reactor에 등록된 핸들맵 객체.
     * @return Nothing
     * @throws IOException IOException 발생시 던진다.
     */
    public void select() throws IOException {
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();

        byte[] buffer = new byte[HEADER_SIZE];
        inputStream.read(buffer);
        String header = new String(buffer);
        
        Runnable protocol;
        Thread thread;
        switch (header) {
        case "0x5001":
        	protocol = new StreamSayHelloProtocol(socket);
            thread = new Thread(protocol);
            thread.start();
        	break;
        case "0x6001":
        	protocol = new StreamUpdateProfileProtocol(socket);
            thread = new Thread(protocol);
            thread.start();
        	break;
        }
    }
}

