package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SelectionKey.*;

/**
 * @author shui4
 * @since 1.0
 */
public class Case7InterestSet {

  public static void main(String[] args) {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        SocketChannel socketChannel = SocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      socketChannel.configureBlocking(false);
      final SelectionKey key1 = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      final SelectionKey key2 = socketChannel.register(selector, OP_CONNECT | OP_READ);
      final SelectionKey key3 = socketChannel.register(selector, OP_CONNECT | OP_READ | OP_WRITE);
      print(key1);
      System.out.println();
      print(key2);
      System.out.println();
      print(key3);
      System.out.println();
      //  重新定义感兴趣的事件
      key3.interestOps(OP_WRITE | OP_CONNECT);
      print(key3);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void print(final SelectionKey key) {
    System.out.println(~key.interestOps() & OP_ACCEPT);
    System.out.println(~key.interestOps() & OP_CONNECT);
    System.out.println(~key.interestOps() & OP_READ);
    System.out.println(~key.interestOps() & OP_WRITE);
  }
}
