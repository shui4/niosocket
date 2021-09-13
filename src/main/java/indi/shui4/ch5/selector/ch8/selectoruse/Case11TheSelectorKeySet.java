package indi.shui4.ch5.selector.ch8.selectoruse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 5.8.11 返回此选择器的键集
 *
 * @author shui4
 * @since 1.0
 */
public class Case11TheSelectorKeySet {

  public static void main(String[] args) throws IOException {
    final Selector selector = Selector.open();
    final SelectionKey key1 = getServerSocketChannel(selector, 7777);
    final SelectionKey key2 = getServerSocketChannel(selector, 8888);
    final SelectionKey key3 = getServerSocketChannel(selector, 9999);
    System.out.println(key1.hashCode());
    System.out.println(key2.hashCode());
    System.out.println(key3.hashCode());
    System.out.println();
    final Set<SelectionKey> keysSet = selector.keys();
    final Iterator<SelectionKey> iterator = keysSet.iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next().hashCode());
    }
  }

  private static SelectionKey getServerSocketChannel(final Selector selector, final int port)
      throws IOException {
    final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress(port));
    return serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
  }
}
