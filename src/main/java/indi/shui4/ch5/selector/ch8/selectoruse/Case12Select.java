package indi.shui4.ch5.selector.ch8.selectoruse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * 5.8.12 {@link Selector#select(long)}
 *
 * @author shui4
 * @since 1.0
 */
public class Case12Select {

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
      serverSocketChannel.register(selector, OP_ACCEPT);
      boolean isRun = true;
      while (isRun) {
        System.out.println("while (isRun ==true)" + System.currentTimeMillis());
        selector.select(5_000);
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          if (key.isAcceptable()) {
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
