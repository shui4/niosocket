package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * {@link SelectionKey#isReadable()} 5.9.2 判断是否已准备好进行读取
 *
 * @author shui4
 * @since 1.0
 */
public class Case2IsReadyOkReadJudging {
  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(8888));
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      SocketChannel socketChannel = null;

      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            System.out.println("server isAcceptable");
            socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            // SocketChannel注册读事件
            socketChannel.register(selector, SelectionKey.OP_READ);
          }

          if (key.isReadable()) {
            System.out.println("server isReadable()");
            int readLength;
            while (true) {
              ByteBuffer buffer = ByteBuffer.allocate(1000);
              if (!((readLength = socketChannel.read(buffer)) != -1)) break;
              System.out.println(new String(buffer.array(), 0, readLength));
            }
            socketChannel.close();
          }
        }
      }

    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    try (Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, SelectionKey.OP_CONNECT);
      socketChannel.connect(new InetSocketAddress(8888));
      int keyCount = selector.select();
      Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        if (key.isConnectable()) {
          while (!socketChannel.finishConnect()) {}
          System.out.println("client is isConnectable()");
          try (SocketChannel channel = (SocketChannel) key.channel()) {
            channel.write(ByteBuffer.wrap("我来自客户端，你好，服务器！".getBytes()));
          }
        }
      }
      System.out.println("client end!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
