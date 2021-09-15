package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 5.9.1 判断是否允许连接 SelectableChannel 对象
 *
 * @author shui4
 * @since 1.0
 */
public class Case1IsAllowConnectionSelectableChannelObjectJudging {
  /** {@link SelectionKey#isAcceptable()} */
  @Test
  public void case1IsAcceptable() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.bind(new InetSocketAddress(8888));
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            final Socket socket = channel.socket().accept();
            System.out.println("server isAcceptable()");
            socket.close();
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case2IsConnectable() {
    try (Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.connect(new InetSocketAddress(8888));
      socketChannel.register(selector, SelectionKey.OP_CONNECT);

      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isConnectable()) {
            System.out.println("client isConnectable()");
            // 需要在此使用 finishConnect() 方法完成连接
            // 因为socketChannel是非阻塞模式
            while (!socketChannel.finishConnect()) {
              System.out.println("!socketChannel finishConnect()");
            }
            final SocketChannel channel = (SocketChannel) key.channel();
            channel.close();
          }
        }
        System.out.println("");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
