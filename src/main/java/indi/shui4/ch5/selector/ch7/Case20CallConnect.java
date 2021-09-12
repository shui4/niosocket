package indi.shui4.ch5.selector.ch7;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * socketChannel
 *
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("DuplicatedCode")
public class Case20CallConnect {

  @Test
  public void server() {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8088));
      final SocketChannel socketChannel = channel.accept();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 以阻塞模式进行连接操作 */
  @Test
  public void client1() {
    Stopwatch stopwatch = null;
    boolean connectResult = false;
    try (SocketChannel socketChannel = SocketChannel.open()) {
      stopwatch = Stopwatch.createStarted();
      connectResult = socketChannel.connect(new InetSocketAddress("localhost", 8088));
      System.out.println("正常连接耗时:" + stopwatch.stop() + ",connectResult=" + connectResult);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("异常连接耗时:" + stopwatch.stop() + ",connectResult=" + connectResult);
    }
  }

  @Test
  public void client2() {
    Stopwatch stopwatch = null;
    boolean connectResult = false;
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      stopwatch = Stopwatch.createStarted();
      connectResult = socketChannel.connect(new InetSocketAddress("localhost", 8088));
      System.out.println("正常连接耗时:" + stopwatch.stop() + ",connectResult=" + connectResult);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("异常连接耗时:" + stopwatch.stop() + ",connectResult=" + connectResult);
    }
  }
}
