package indi.shui4.ch5.selector.ch8.selectoruse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 5.8.8 对相同的通道注册不同的相关事件返回同一个 SelectionKey
 *
 * @author shui4
 * @since 1.0
 */
public class Case8 {
  @Test
  public void server() throws IOException {
    final Selector selector = Selector.open();
    final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress("localhost", 7777));

    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    while (true) {
      final int keyCount = selector.select();
      final Set<SelectionKey> set1 = selector.keys();
      final Set<SelectionKey> set2 = selector.selectedKeys();
      System.out.println("keyCount=" + keyCount);
      System.out.println("set1.size()=" + set1.size());
      System.out.println("set2.size()=" + set2.size());
      System.out.println();
      final Iterator<SelectionKey> iterator = set2.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        iterator.remove();
        if (key.isAcceptable()) {
          final ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
          final SocketChannel socketChannel = serverSocketChannel1.accept();
          socketChannel.configureBlocking(false);
          final SelectionKey key2 = socketChannel.register(selector, SelectionKey.OP_READ);
          System.out.println(
              "key2.isReadable()=" + ((SelectionKey.OP_READ & ~key2.interestOps()) == 0));

          System.out.println(
              "key2.isWriteable()=" + ((SelectionKey.OP_WRITE & ~key2.interestOps()) == 0));

          final SelectionKey key3 =
              socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
          System.out.println(
              "key3.isReadable()=" + ((SelectionKey.OP_READ & ~key3.interestOps()) == 0));

          System.out.println(
              "key3.isWriteable()=" + ((SelectionKey.OP_WRITE & ~key3.interestOps()) == 0));
          System.out.println("keyCountB=" + keyCount);
          System.out.println("set1 size=" + set1.size());
          System.out.println("set2 size=" + set2.size());
          System.out.println("key2==key3结果：" + (key2 == key3));
        }
        if (key.isReadable()) {
          final SocketChannel socketChannel = (SocketChannel) key.channel();
          final ByteBuffer buffer = ByteBuffer.allocate(1024);
          while (socketChannel.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
              System.out.println((char) buffer.get());
            }
            buffer.clear();
          }
          key.cancel();
        }
      }
    }
  }

  @Test
  public void client() {
    try (Socket socket = new Socket("localhost", 7777)) {
      socket.getOutputStream().write("12345".getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
