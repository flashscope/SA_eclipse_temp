import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.log4j.Logger;




public class Demultiplexer implements Runnable {
    private final int HEADER_SIZE = 6;
    private Socket socket;
    private HandleMap handlers;
    
    protected static Logger logger = Logger.getLogger(Demultiplexer.class.getName());
    
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
			//DEBUG < INFO < WARN < ERROR < FATAL

	        logger.fatal("log4j:logger.fatal()");

	        logger.error("log4j:logger.error()");

	        logger.warn("log4j:logger.warn()");

	        logger.info("log4j:logger.info()");

	        logger.debug("log4j:logger.debug()");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
