

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @brief 이벤트 핸들러를 관리하는 객체.
 * @details 이벤트 핸들러 자원을 관리한다. Demultiplexer에 select 메세지를 보낸다.
 */
public class Reactor {
    private ServerSocket serverSocket;
    private HandleMap handlers;

    /**
     * @brief Reactor 생성자.
     * @details 핸들과 이벤트 핸들러를 관리할 핸들맵을 초기화한다.
     * @return Nothing
     */
    public Reactor(int servPort) throws IOException {
        handlers = new HandleMap();
        serverSocket = new ServerSocket(servPort);
    }

    /**
     * @brief Demultiplexer에 select 명령을 한다.
     * @details Demultiplexer에 핸들러들을 전달한다. 반복적으로 수행한다.
     * @return Nothing
     * @throws IOException IOException 발생시 던진다.
     */
    public void handle_events() throws IOException {
    	while (true) {
        	Socket socket = serverSocket.accept();
        	Runnable demultiplexer = new Demultiplexer(socket, handlers);
        	Thread thread = new Thread(demultiplexer);
        	thread.start();
    	}
    }

    /**
     * @brief Reactor에 이벤트 핸들러를 등록한다.
     * @details handler에서 handle을 반환받는다. 반환받은 handle을 키값으로 handler를 저장한다.
     * @param handler 특정 이벤트를 처리할 핸들러
     * @return Nothing
     */
    public void registerHandler(EventHandler handler) {
        handlers.put(handler.getHandle(), handler);
    }

    /**
     * @brief Reactor에 이벤트 핸들러를 제거한다.
     * @details handler에서 handle을 반환받는다. 반환받은 handle을 키값으로 handler를 제거한다.
     * @param handler 특정 이벤트를 처리할 핸들러
     * @return Nothing
     */
    public void removeHandler(EventHandler handler) {
        handlers.remove(handler.getHandle());
    }
}