package indi.shui4.ch5.selector.ch8.selectoruse;

import org.junit.jupiter.api.Test;

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
 * 5.8.6 int selector.select()方法返回值的含义
 *
 * @author shui4
 * @since 1.0
 */
public class Case6SelectMethodReturnValueTheMeaningOf {

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        final ServerSocketChannel serverSocketChannel1 = buildServerSocketChannel(7777, selector);
        final ServerSocketChannel serverSocketChannel2 = buildServerSocketChannel(8888, selector);
        final ServerSocketChannel serverSocketChannel3 = buildServerSocketChannel(9999, selector)) {
      while (true) {
        final int keyCount = selector.select();
        final Set<SelectionKey> set1 = selector.keys();
        final Set<SelectionKey> set2 = selector.selectedKeys();
        System.out.println("keyCount=" + keyCount);
        System.out.println("set1 size=" + set1.size());
        System.out.println("set2 size=" + set2.size());
        System.out.println();
        final Iterator<SelectionKey> iterator = set2.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
          channel.accept();
        }
        TimeUnit.SECONDS.sleep(10);
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private ServerSocketChannel buildServerSocketChannel(final int port, final Selector selector)
      throws IOException {
    final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress(port));
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    return serverSocketChannel;
  }

  @Test
  public void clientA() {
    try (Socket socket = new Socket("localhost", 7777)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void clientB() {
    try (Socket socket1 = new Socket("localhost", 8888);
        Socket socket2 = new Socket("localhost", 9999)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
