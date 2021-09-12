package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.7.13 获得与设置 SocketOption
 *
 * @author shui4
 * @since 1.0
 */
public class Case13GetAndSetSocketOption {

  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      System.out.println("A SO_RCVBUF=" + channel.getOption(StandardSocketOptions.SO_RCVBUF));
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 5678);
      System.out.println("B SO_RCVBUF=" + channel.getOption(StandardSocketOptions.SO_RCVBUF));
      final ServerSocket socket = channel.socket();
      System.out.println(
          "java.net.ServerSocket.getReceiveBufferSize=" + socket.getReceiveBufferSize());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
