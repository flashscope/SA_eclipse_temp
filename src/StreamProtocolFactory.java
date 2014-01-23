

import java.net.Socket;

public interface StreamProtocolFactory {
  public Runnable createProtocol(Socket clntSock);
}
