import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


public interface EventHandler extends CompletionHandler<Integer, ByteBuffer> {
    /**
     * @brief 이벤트 핸들러의 키값을 반환한다.
     * @return HandleKey 특정 이벤트 핸들러의 키값
     */
    public String getHandle();
    public int getDataSize();
    
	public void initialize(AsynchronousSocketChannel channel, ByteBuffer buffer);
    
}
