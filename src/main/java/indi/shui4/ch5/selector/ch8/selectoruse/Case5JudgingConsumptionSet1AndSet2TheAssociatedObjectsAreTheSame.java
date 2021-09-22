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

/**
 * 5.8.5 验证产生的 set1 set2 关联的各自对象一直是同一个
 *
 * @author shui4
 * @since 1.0
 */
public class Case5JudgingConsumptionSet1AndSet2TheAssociatedObjectsAreTheSame {

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
        ServerSocketChannel serverSocketChannel2 = ServerSocketChannel.open()) {
      serverSocketChannel1.configureBlocking(false);
      serverSocketChannel1.bind(new InetSocketAddress(7777));
      serverSocketChannel1.register(selector, SelectionKey.OP_ACCEPT);

      serverSocketChannel2.configureBlocking(false);
      serverSocketChannel2.bind(new InetSocketAddress(8888));
      serverSocketChannel2.register(selector, SelectionKey.OP_ACCEPT);

      while (true) {

        final int keyCount = selector.select();

        final Set<SelectionKey> keys = selector.keys();
        final Set<SelectionKey> selectionKeys = selector.selectedKeys();
        System.out.println("keyCount=" + keyCount);
        System.out.println("keys size=" + keys.size());
        System.out.println("selectionKeys size=" + selectionKeys.size());
        System.out.println();
        final Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          //          iterator.remove();
          final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
          channel.accept();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client1() {
    try (Socket socket = new Socket("localhost", 7_777)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client2() {
    try (Socket socket = new Socket("localhost", 8_888)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
