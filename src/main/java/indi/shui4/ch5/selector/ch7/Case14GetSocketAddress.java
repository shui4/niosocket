package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.7.14 获得 SocketAddress 对象
 *
 * @author shui4
 * @since 1.0
 */
public class Case14GetSocketAddress {
  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress("localhost", 8888));
      final InetSocketAddress localAddress = (InetSocketAddress) channel.getLocalAddress();
      System.out.println(localAddress.getHostString());
      System.out.println(localAddress.getPort());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
