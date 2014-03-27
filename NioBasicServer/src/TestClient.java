import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {

	public static void main(String[] args) {
		System.out.println("Client ON");
		int count = 0;
		while (true) {
			try {
				String message;
				++count;
				Socket socket = new Socket("127.0.0.1", 5000);
				OutputStream out = socket.getOutputStream();
				message = "0x5001|홍길동" + count + "|22";
				out.write(message.getBytes());
				socket.close();

				Thread.sleep(1000);
				
				Socket socket2 = new Socket("127.0.0.1", 5000);
				OutputStream out2 = socket2.getOutputStream();
				message = "0x6001|hong|1234|홍길동" + count + "|22|남성";
				out2.write(message.getBytes());
				socket2.close();
				
				Thread.sleep(2000);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
