package indi.shui4.ch5.selector.ch8.selectoruse;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * @author shui4
 * @since 1.0
 */
public class Case13SelectNow {

  public static void main(String[] args) {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.register(selector, OP_ACCEPT);
      while (true) {
        System.out.println(System.currentTimeMillis());
        final int keyCount = selector.selectNow();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

        while (iterator.hasNext()) {
          System.out.println("进入while");
          SelectionKey key = iterator.next();
          if (key.isReadable()) {
            final ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
            final Socket socket = serverSocketChannel1.socket().accept();
            socket.close();
          }
          iterator.remove();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
