import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

class StreamUpdateProfileEventHandler implements EventHandler, CompletionHandler<Integer, ByteBuffer> {
	private static Charset utf8 = Charset.forName("utf-8");
	private static final int TOKEN_NUM = 5;
	
	private AsynchronousSocketChannel channel;
	private ByteBuffer buffer;

	@Override
	public String getHandle() {
		 return "0x6001";
	}
	
	@Override
	public int getDataSize() {
		return 1024;
	}
	
	public void initialize(AsynchronousSocketChannel channel, ByteBuffer buffer) {
		this.channel = channel;
		this.buffer = buffer;
	}

	@Override
	public void completed(Integer result, ByteBuffer buff) {
		if (result == -1) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (result > 0) {
			buffer.flip();
			String msg = utf8.decode(buffer).toString();
			
			String[] params = new String[TOKEN_NUM];
			StringTokenizer token = new StringTokenizer(msg, "|");
	        int i = 0;
	        while (token.hasMoreTokens()) {
	            params[i] = token.nextToken();
	            i++;
	        }
	        
	        updateProfile(params);
	        
	        try {
	        	buffer.clear();
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer buff) {
		// TODO Auto-generated method stub
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