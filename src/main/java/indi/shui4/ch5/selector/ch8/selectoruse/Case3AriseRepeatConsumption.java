package indi.shui4.ch5.selector.ch8.selectoruse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 5.8.3 出现重复消费的情况
 *
 * @author shui4
 * @since 1.0
 */
public class Case3AriseRepeatConsumption {

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
        ServerSocketChannel serverSocketChannel2 = ServerSocketChannel.open(); ) {

      serverSocketChannel1.configureBlocking(false);
      serverSocketChannel1.bind(new InetSocketAddress("localhost", 7_777));
      serverSocketChannel1.register(selector, SelectionKey.OP_ACCEPT);

      serverSocketChannel2.configureBlocking(false);
      serverSocketChannel2.bind(new InetSocketAddress("localhost", 8_888));
      serverSocketChannel2.register(selector, SelectionKey.OP_ACCEPT);

      while (true) {
        final int keyCount = selector.select();
        final Set<SelectionKey> keys = selector.keys();
        final Set<SelectionKey> selectionKeys = selector.selectedKeys();
        System.out.println("keyCount=" + keyCount);
        System.out.println("keys size=" + keys.size());
        System.out.println("selectionKeys size=" + selectionKeys.size());
        final Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
          final SocketChannel socketChannel = channel.accept();
          if (socketChannel == null) {
            System.out.println("打印这条信息证明是连接8888服务器时，重复消费的情况，");
            System.out.println("将7777关联的SelectionKey对应的SocketChannel通道取出来，");
            System.out.println("但是指为null，socketChannel == null");
          }
          final InetSocketAddress ipAddress = (InetSocketAddress) channel.getLocalAddress();
          System.out.println(ipAddress.getPort() + " 被客户端连接了！");
          System.out.println();
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
