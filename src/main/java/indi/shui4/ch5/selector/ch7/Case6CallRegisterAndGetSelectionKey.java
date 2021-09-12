package indi.shui4.ch5.selector.ch7;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * 5.7.6 执行注册操作与获得 SelectionKey 对象
 *
 * @author shui4
 * @since 1.0
 */
public class Case6CallRegisterAndGetSelectionKey {

  @Test
  public void main() {
    try (final ServerSocketChannel channel = ServerSocketChannel.open()) {
      // 必须将    ServerSocketChannel 设置成非阻塞的，
      // 不然出现：java.nio.channels.IllegalBlockingModeException
      channel.configureBlocking(false);
      ServerSocket serverSocket = channel.socket();
      serverSocket.bind(new InetSocketAddress(8888));

      try (final Selector selector = Selector.open()) {
        SelectionKey selectionKey = channel.register(selector, OP_ACCEPT);
        System.out.println("selector=" + selector);
        System.out.println("selectionKey=" + selectionKey);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
