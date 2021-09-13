package indi.shui4.ch5.selector.ch8.selectoruse;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * 5.8.1 验证select方法具有阻塞特性
 *
 * @author shui4
 * @since 1.0
 */
public class Case1SelectMethodIsBlock {

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      System.out.println(1);
      serverSocketChannel.bind(new InetSocketAddress(8088));
      System.out.println(2);
      serverSocketChannel.configureBlocking(false);
      System.out.println(3);
      System.out.println(4);
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      System.out.println(5);
      final Stopwatch stopwatch = Stopwatch.createStarted();
      final int keyCount = selector.select();
      System.out.println("java.nio.channels.Selector.select()阻塞耗时：" + stopwatch.stop());

      System.out.println("6 keyCount=" + keyCount);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("7 end!");
  }

  @Test
  public void client() throws InterruptedException {
    TimeUnit.SECONDS.sleep(4);
    try (Socket socket = new Socket("localhost", 8088)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
