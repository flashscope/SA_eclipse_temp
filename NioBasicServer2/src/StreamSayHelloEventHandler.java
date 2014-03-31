import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

class StreamSayHelloEventHandler implements EventHandler, CompletionHandler<Integer, ByteBuffer> {
	private static Charset utf8 = Charset.forName("utf-8");
	private static final int TOKEN_NUM = 2;
	
	private AsynchronousSocketChannel channel;
	private ByteBuffer buffer;

	@Override
	public String getHandle() {
		 return "0x5001";
	}
	
	@Override
	public int getDataSize() {
		return 512;
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
	        sayHello(params);
	        

	        
	        /*
			Future<Integer> w = channel.write(utf8.encode(msg));
			try {
				w.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			buffer.clear();
			channel.read(buff, buff, this);
			*/
			
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
	
	
	private void sayHello(String[] params) {
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("mm:ss");
		String str = dayTime.format(new Date(time));
        System.out.println("SayHello -> name : " + params[0] + " age : " + params[1]+" resultTime:" + str);
    }




}