package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.7.15 阻塞模式的判断
 *
 * @author shui4
 * @since 1.0
 */
public class Case15BlockModeJudge {

  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress("localhost", 8888));
      System.out.println(channel.isBlocking());
      channel.configureBlocking(false);
      System.out.println(channel.isBlocking());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
