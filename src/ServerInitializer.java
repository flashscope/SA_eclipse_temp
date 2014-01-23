

import java.io.IOException;

/**
 * @author Michael J.Donahoo, Kenneth L.Calvert
 * @editor Dongkuk Kim, dongkuk5411@nhnnext.org
 * @version 0.0.1
 * @brief 서버가 시작하는 클래스
 * @details 소켓, 로거, 프로토콜 팩토리를 디스패처에 전달한다.
 * @date 2014-01-16
 */
public class ServerInitializer {

    /**
     * @param args 포트번호, 프로토콜명, 디스패처명이 있는 콘솔 입력 함수
     * @throws Exception
     * @brief 메인 메서드
     * @details 콘솔에서 포트번호, 프로토콜명, 디스패처명을 입력받아 적절한 소켓, 프로토콜팩토리, 디스패처를 생성한다.
     */
    public static void main(String[] args) throws Exception {

        int servPort = 5000;

        ServerInitializer serverInitializer = new ServerInitializer();
        serverInitializer.initializeServer(servPort);
    }

    public void initializeServer(int servPort) throws IOException {
    	System.out.println("Server ON");
        Dispatcher dispatcher = new Dispatcher(servPort);
        while (true) {
        	dispatcher.select();
        }
    }
}
