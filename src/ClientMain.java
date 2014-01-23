

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Michael J.Donahoo, Kenneth L.Calvert
 * @editor Dongkuk Kim, dongkuk5411@nhnnext.org
 * @version 0.0.1
 * @brief 서버에 요청을 보내는 클라이언트 클래스
 * @details 서버에 에코 프로토콜로 요청을 보낸다.
 * @date 2014-01-16
 */
public class ClientMain {

	 public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
	        System.out.println("Client ON");
	        while (true) {
	            {
	                Socket socket = new Socket("127.0.0.1", 5000);
	                OutputStream out = socket.getOutputStream();
	                out.write("0x5001|김동국|22".getBytes());
	                socket.close();
	            }
	            {
	                Socket socket = new Socket("127.0.0.1", 5000);
	                OutputStream out = socket.getOutputStream();

	                out.write("0x6001|dongkuk5411|ehdrnrcjswo1!|김동국|22|남성".getBytes());
	                socket.close();
	            }
	            Thread.sleep(1000);
	        }
	    }
}
