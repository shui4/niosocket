package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 4. 删除键集中的键会导致 UnsupportedOperationException 异常
 *
 * @author shui4
 * @since 1.0
 */
public class Test4 {

  public static void main(String[] args) {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.bind(new InetSocketAddress(7777));
      serverSocketChannel.configureBlocking(false);
      final SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      selector.keys().remove(key);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
