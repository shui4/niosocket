package indi.shui4.ch5.selector.ch8.selectoruse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 5.8.14 唤醒操作
 * @author shui4
 * @since 1.0
 */
public class Case14NotifyOperational {

  private static volatile Selector selector;

  public static void main(String[] args) {

    new Thread(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(2);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              selector.wakeup();
              final Set<SelectionKey> set1 = selector.keys();
              final Set<SelectionKey> set2 = selector.selectedKeys();
              System.out.println("执行 wakeup()方法之后的selector的消息");
              System.out.println("set1.size()=" + set1.size());
              System.out.println("set2.size()=" + set2.size());
            })
        .start();

    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      selector = Selector.open();
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(8888));
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      selector.select();
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

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
