package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.9.4 返回 SelectionKey 关联的选择器
 *
 * @author shui4
 * @since 1.0
 */
public class Case4SelectionKeyAssociateSelector {

  @Test
  public void server() {
    try (Selector selector1 = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(8888));
      SelectionKey selectionKey1 = serverSocketChannel.register(selector1, SelectionKey.OP_ACCEPT);
      Selector selector2 = selectionKey1.selector();

      System.out.println(selector1 + " " + selector1.hashCode());
      System.out.println(selector2 + " " + selector2.hashCode());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
