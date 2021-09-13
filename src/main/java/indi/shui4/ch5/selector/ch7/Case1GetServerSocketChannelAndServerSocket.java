package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.7.1 获得 ServerSocketChannel 与 ServerSocket socket 对象 <br>
 * {@link ServerSocketChannel#open()}
 *
 * @author shui4
 * @since 1.0
 */
public class Case1GetServerSocketChannelAndServerSocket {

  public static void main(String[] args) {
    try (final ServerSocketChannel channel = ServerSocketChannel.open()) {
      final ServerSocket serverSocket = channel.socket();
      serverSocket.bind(new InetSocketAddress(8888));
      try (final Socket socket = serverSocket.accept()) {
        final InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        int readLength;
        final char[] chars = new char[1024];
        while ((readLength = reader.read(chars)) != -1) {
          System.out.println(new String(chars, 0, readLength));
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
