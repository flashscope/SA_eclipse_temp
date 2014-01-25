import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class Demultiplexer implements Runnable {
    private final int HEADER_SIZE = 6;
    private Socket socket;
    private HandleMap handlers;
    /**
     * @brief 이벤트 발생시 Demultiplex 한다.
     * @details 이벤트 발생시 헤더부분만큼을 읽어 각 헤더에 맞는 Protocol을 실행시킨다.
     * @return Nothing
     * @throws IOException IOException 발생시 던진다.
     */
    public Demultiplexer(Socket socket, HandleMap handlers) throws IOException {
        this.socket = socket;
        this.handlers = handlers;
    }

	@Override
	public void run() {
		InputStream inputStream;
		try {
			inputStream = socket.getInputStream();

			byte[] buffer = new byte[HEADER_SIZE];
			inputStream.read(buffer);
			String header = new String(buffer);
			
			handlers.get(header).handleEvent(inputStream);
			
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
