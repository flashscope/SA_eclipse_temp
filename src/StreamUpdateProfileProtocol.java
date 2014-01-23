import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.StringTokenizer;


public class StreamUpdateProfileProtocol implements Runnable {
    private static final int DATA_SIZE = 1024;
    private static final int TOKEN_NUM = 5;
    
    private Socket clntSock;


    /**
     * @param clntSock 클라이언트 소켓
     * @param logger 로그 클래스
     * @brief 에코 프로토콜의 생성자
     * @details 클라이언트의 요청을 처리할 에코 프로토콜 객체를 생성한다.
     */
    public StreamUpdateProfileProtocol(Socket clntSock) {
        this.clntSock = clntSock;
    }
    
	@Override
	public void run() {
		InputStream data;
		try {
			data = clntSock.getInputStream();

			byte[] buffer = new byte[DATA_SIZE];
			data.read(buffer);

			String str = new String(buffer);
			String[] params = new String[TOKEN_NUM];

			StringTokenizer token = new StringTokenizer(str, "|");
			int i = 0;
			while (token.hasMoreTokens())
				params[i++] = token.nextToken();

			updateProfile(params);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     * @brief 파라미터를 출력한다.
     * @param params updateProfile할 파라미터. 아이디, 패스워드, 이름, 나이, 성별 정보가 있다.
     * @return Nothing
     * @description 사용자의 정보를 업데이트하는 이벤트 핸들러
     */
    public void updateProfile(String[] params) {
        System.out.println("UpdateProfile -> " +
                " id :" + params[0] +
                " password : " + params[1] +
                " name : " + params[2] +
                " age : " + params[3] +
                " gender: " + params[4]);
    }
}
