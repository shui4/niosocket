package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class Case10Cancel {
  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.bind(new InetSocketAddress(8888));
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      new Thread(
              () -> {
                try {
                  TimeUnit.SECONDS.sleep(3);
                  System.out.println(
                      "cancel() after selector.keys().size()=" + selector.keys().size());
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              })
          .start();
      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        System.out.println("cancel() before selector.keys().size()=" + selector.keys().size());
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            final SocketChannel socketChannel = channel.accept();
          }
          key.cancel();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.connect(new InetSocketAddress(8888));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
