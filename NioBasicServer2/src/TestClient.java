import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestClient {

	public static void main(String[] args) {
		System.out.println("Client ON");
		int count = 0;
		while (true) {
			try {
				String message;
				++count;
				
				long time = System.currentTimeMillis(); 
				SimpleDateFormat dayTime = new SimpleDateFormat("mm:ss");
				String str = dayTime.format(new Date(time));
				
				
				Socket socket = new Socket("127.0.0.1", 5000);
				OutputStream out = socket.getOutputStream();
				message = "0x5001|홍길동" + count+"[" + str+"]" + "|22";
				out.write(message.getBytes());
				socket.close();

				Thread.sleep(10);
				
				Socket socket2 = new Socket("127.0.0.1", 5000);
				OutputStream out2 = socket2.getOutputStream();
				message = "0x6001|hong|1234|홍길동" + count + "|22|남성";
				out2.write(message.getBytes());
				
				socket2.close();
				
				Thread.sleep(200);
				
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
