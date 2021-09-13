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
 * 5.8.2 select()方法不阻塞的原因和解决办法
 *
 * @author shui4
 * @since 1.0
 */
public class Case2SelectMethodNoBlockCauseAndQA {
  /** 不将事件处理造成死循环 */
  @Test
  public void server1() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      while (true) {
        final int keyCount = selector.select();
        final Set<SelectionKey> keys = selector.keys();
        final Set<SelectionKey> selectionKeys = selector.selectedKeys();
        System.out.println("keyCount=" + keyCount);
        System.out.println("keys.size()=" + keys.size());
        System.out.println("selectionKeys.size()=" + selectionKeys.size());
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 解决死循环，使用ServerSocketChannel#accept()将事件处理掉 */
  @Test
  public void server2() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      while (true) {
        final int keyCount = selector.select();
        final Set<SelectionKey> keys = selector.keys();
        final Set<SelectionKey> selectionKeys = selector.selectedKeys();
        System.out.println("keyCount=" + keyCount);
        System.out.println("keys.size()=" + keys.size());
        System.out.println("selectionKeys.size()=" + selectionKeys.size());
        final Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
          channel.accept();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    try (Socket socket = new Socket("localhost", 8888)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
