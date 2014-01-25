import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class Demultiplexer implements Runnable {
    private final int HEADER_SIZE = 6;
    private Socket socket;
    /**
     * @brief 이벤트 발생시 Demultiplex 한다.
     * @details 이벤트 발생시 헤더부분만큼을 읽어 각 헤더에 맞는 Protocol을 실행시킨다.
     * @return Nothing
     * @throws IOException IOException 발생시 던진다.
     */
    public Demultiplexer(Socket socket) throws IOException {
        this.socket = socket;
    }

	@Override
	public void run() {
		InputStream inputStream;
		try {
			inputStream = socket.getInputStream();

			byte[] buffer = new byte[HEADER_SIZE];
			inputStream.read(buffer);
			String header = new String(buffer);
			
			ProtocolFactory protocol;
			switch (header) {
			case "0x5001":
				protocol = new StreamSayHelloProtocol(socket);
				protocol.readData();
				break;
			case "0x6001":
				protocol = new StreamUpdateProfileProtocol(socket);
				protocol.readData();
				break;
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
