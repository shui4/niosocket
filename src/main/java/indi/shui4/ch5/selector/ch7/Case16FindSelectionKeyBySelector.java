package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.7.16 根据 Selector 找到对应的 SelectionKey
 *
 * @author shui4
 * @since 1.0
 */
public class Case16FindSelectionKeyBySelector {

  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8888));
      channel.configureBlocking(false);
      final Selector selector = Selector.open();
      final SelectionKey key = channel.register(selector, SelectionKey.OP_ACCEPT);
      System.out.println("A=" + key.hashCode());
      System.out.println("B=" + channel.keyFor(selector).hashCode());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
