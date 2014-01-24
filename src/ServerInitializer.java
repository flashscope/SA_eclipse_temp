

import java.io.IOException;

/**
 * @author Michael J.Donahoo, Kenneth L.Calvert
 * @editor Dongkuk Kim, dongkuk5411@nhnnext.org
 * @version 0.0.1
 * @brief 서버가 시작하는 클래스
 * @details 소켓을 디스패처에 전달한다.
 * @date 2014-01-16
 */
public class ServerInitializer {

    public static void main(String[] args) throws Exception {

        int servPort = 5000;

        ServerInitializer serverInitializer = new ServerInitializer();
        serverInitializer.initializeServer(servPort);
    }

    public void initializeServer(int servPort) throws IOException {
    	System.out.println("Server ON");
        Dispatcher dispatcher = new Dispatcher(servPort);
        while (true) {
        	dispatcher.demultiplex();
        }
    }
}
