package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * 5.7.9 使用 {@link AbstractSelectableChannel#configureBlocking(boolean)} 传入false解决决异常
 *
 * @author shui4
 * @since 1.0
 */
public class Case9UseConfigureBlockingTheFalseSolveException {
  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8888));
      System.out.println("A isBlocking=" + channel.isBlocking());
      channel.configureBlocking(false);
      System.out.println("B isBlocking=" + channel.isBlocking());
      try (Selector selector = Selector.open()) {
        System.out.println("A isRegistered=" + channel.isRegistered());
        SelectionKey key = channel.register(selector, OP_ACCEPT);
        System.out.println("B isRegistered=" + channel.isRegistered());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
