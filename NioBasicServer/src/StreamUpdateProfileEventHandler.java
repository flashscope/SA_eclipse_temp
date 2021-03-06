import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class StreamUpdateProfileEventHandler implements EventHandler {
	private static final int DATA_SIZE = 1024;
    private static final int TOKEN_NUM = 5;
    private static final int READ_MESSAGE_WAIT_TIME = 15;
    
	@Override
	public String getHandle() {
		return "0x6001";
	}

	@Override
	public void handleEvent(AsynchronousSocketChannel asynchronousSocketChannel) {
		try {

			ByteBuffer messageByteBuffer = ByteBuffer.allocate(DATA_SIZE);
			Future<Integer> futureReadResult = asynchronousSocketChannel.read(messageByteBuffer);
			futureReadResult.get(READ_MESSAGE_WAIT_TIME, TimeUnit.SECONDS);
			
			// 받아온 데이터를 String으로 변환
			String str = new String(messageByteBuffer.array()).trim();
	        String[] params = new String[TOKEN_NUM];

	        StringTokenizer token = new StringTokenizer(str, "|");
	        int i = 0;
	        while (token.hasMoreTokens()) {
	            params[i] = token.nextToken();
	            i++;
	        }
	        updateProfile(params);
		} catch (InterruptedException | ExecutionException
				| TimeoutException e) {
			e.printStackTrace();
		}
	}
    private void updateProfile(String[] params) {
        System.out.println("UpdateProfile -> " +
                " id :" + params[0] +
                " password : " + params[1] +
                " name : " + params[2] +
                " age : " + params[3] +
                " gender: " + params[4]);
    }


    
}

