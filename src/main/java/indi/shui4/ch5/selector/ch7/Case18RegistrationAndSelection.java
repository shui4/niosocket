package indi.shui4.ch5.selector.ch7;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * 5.7.18 通道注册与选择器
 *
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("DuplicatedCode")
public class Case18RegistrationAndSelection {
  /** 1. 相同的通道可以注册到不同的选择器，返回的 Selection Key 不是同一个对象 */
  @Test
  public void case1() {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8888));
      channel.configureBlocking(false);
      final Selector selector1 = Selector.open();
      final Selector selector2 = Selector.open();
      final SelectionKey key1 = channel.register(selector1, OP_ACCEPT);
      final SelectionKey key2 = channel.register(selector2, OP_ACCEPT);
      System.out.println(key1.hashCode());
      System.out.println(key2.hashCode());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 2. 不同的通道注册到相同的选择器，返回的 Selection Key 不是同一个对象 */
  @Test
  public void case2() throws IOException {
    // 1
    ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
    serverSocketChannel1.configureBlocking(false);
    // 2
    ServerSocketChannel serverSocketChannel2 = ServerSocketChannel.open();
    serverSocketChannel2.configureBlocking(false);
    // -----
    final Selector selector = Selector.open();
    final SelectionKey key1 = serverSocketChannel1.register(selector, OP_ACCEPT);
    final SelectionKey key2 = serverSocketChannel2.register(selector, OP_ACCEPT);
    System.out.println(key1.hashCode());
    System.out.println(key2.hashCode());
  }
  /** 3. 不同的通道注册到不同的选择器，返回的 Selection key 不是同一个对象 */
  @Test
  public void case3() throws IOException {
    // 1
    ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
    serverSocketChannel1.configureBlocking(false);
    // 2
    ServerSocketChannel serverSocketChannel2 = ServerSocketChannel.open();
    serverSocketChannel2.configureBlocking(false);
    // -----
    final Selector selector1 = Selector.open();
    final Selector selector2 = Selector.open();
    final SelectionKey key1 = serverSocketChannel1.register(selector1, OP_ACCEPT);
    final SelectionKey key2 = serverSocketChannel2.register(selector2, OP_ACCEPT);
    System.out.println(key1.hashCode());
    System.out.println(key2.hashCode());
  }

  /** 4.相同的通道重复注册相同的选择器，返回的SelectionKey是同一个对象 */
  @Test
  public void case4() throws IOException {
    final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    final Selector selector = Selector.open();
    final SelectionKey key1 = serverSocketChannel.register(selector, OP_ACCEPT);
    final SelectionKey key2 = serverSocketChannel.register(selector, OP_ACCEPT);
    System.out.println(key1 == key2);
  }
}
