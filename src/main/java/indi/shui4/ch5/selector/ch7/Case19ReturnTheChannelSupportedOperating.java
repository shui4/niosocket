package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 5.7.19 返回此通道所支持的操作
 *
 * @author shui4
 * @since 1.0
 */
public class Case19ReturnTheChannelSupportedOperating {
  public static void main(String[] args) {
    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        final SocketChannel socketChannel = SocketChannel.open()) {
      final int value1 = serverSocketChannel.validOps();
      final int value2 = socketChannel.validOps();

      System.out.println("value1=" + value1);
      System.out.println("value2=" + value2);
      System.out.println();
      // ServerSocketChannel只支持OP_ACCEPT
      System.out.println(SelectionKey.OP_ACCEPT & ~serverSocketChannel.validOps());
      System.out.println(SelectionKey.OP_CONNECT & ~serverSocketChannel.validOps());
      System.out.println(SelectionKey.OP_READ & ~serverSocketChannel.validOps());
      System.out.println(SelectionKey.OP_WRITE & ~serverSocketChannel.validOps());
      System.out.println();
      // SocketChannel支持OP_CONNECT、OP_READ、OP_WRITE
      System.out.println(SelectionKey.OP_ACCEPT & ~socketChannel.validOps());
      System.out.println(SelectionKey.OP_CONNECT & ~socketChannel.validOps());
      System.out.println(SelectionKey.OP_READ & ~socketChannel.validOps());
      System.out.println(SelectionKey.OP_WRITE & ~socketChannel.validOps());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
