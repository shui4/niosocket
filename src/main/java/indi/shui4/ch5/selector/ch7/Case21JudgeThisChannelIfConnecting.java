package indi.shui4.ch5.selector.ch7;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 5.7.21 判断此通道上是否正在进行连接操作
 *
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("DuplicatedCode")
public class Case21JudgeThisChannelIfConnecting {

  @Test
  public void server() {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8088));
      try (SocketChannel socketChannel = channel.accept()) {}

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 阻塞通道，IP不存在 */
  @Test
  public void client1() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      System.out.println(socketChannel.isConnectionPending());
      socketChannel.connect(new InetSocketAddress("192.168.0.123", 8088));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 非阻塞通道,IP不存在 */
  @Test
  public void client2() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      System.out.println(socketChannel.isConnectionPending());
      socketChannel.connect(new InetSocketAddress("192.168.0.123", 8088));
      // true：说明非阻塞通道正在建立连接
      System.out.println(socketChannel.isConnectionPending());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 阻塞通道,IP存在 */
  @Test
  public void client3() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      System.out.println(socketChannel.isConnectionPending());
      socketChannel.connect(new InetSocketAddress("localhost", 8088));
      // false：说明阻塞通道并没有正在建立连接
      System.out.println(socketChannel.isConnectionPending());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 非阻塞通道，IP存在 */
  @Test
  public void client4() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      System.out.println(socketChannel.isConnectionPending());
      socketChannel.connect(new InetSocketAddress("localhost", 8088));
      // true：说明非阻塞通道正在建立连接
      System.out.println(socketChannel.isConnectionPending());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
