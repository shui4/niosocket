package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * 5.7.8 将通道设置成非阻塞模式再注册到选择器 <br>
 * 这里会出现异常：出现异常的原因是没有将通道设置成非阻塞模式。如果想把通道注册到选择器中，就必须将通道设置成非阻塞模式。
 *
 * @author shui4
 * @since 1.0
 */
public class Case8WillChannelSetNoBlockModeThenRegisterToSelector {
  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8888));
      try (Selector selector = Selector.open()) {
        SelectionKey key = channel.register(selector, OP_ACCEPT);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
