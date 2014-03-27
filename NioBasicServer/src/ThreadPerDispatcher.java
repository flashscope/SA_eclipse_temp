import java.nio.channels.AsynchronousSocketChannel;


public class ThreadPerDispatcher implements Dispatcher {
	/**
     * @param servSock     서버 소켓 클래스
     * @param logger       로그 남기는 클래스
     * @param protoFactory 프로토콜 생성 클래스
     * @brief 디스패칭하는 메서드
     * @details 클라이언트의 요청을 받으면 쓰레드를 추가로 생성하고 적절한 프로토콜 클래스에 이벤트 처리 작업을 넘긴다.
     */
	@Override
	public void startDispatching(AsynchronousSocketChannel asynchronousSocketChannel, HandleMap handlers) {
        
        Runnable demultiplexer = new Demultiplexer(asynchronousSocketChannel, handlers);
		demultiplexer.run();
	}
}
