package indi.shui4.ch5.selector.ch7;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 5.7.4 阻塞与非阻塞以及 accept（）方法的使用效果
 *
 * @author shui4
 * @since 1.0
 */
public class Case4NoBlockAndBlockedAcceptMethod {
  @Test
  public void case1BlockServer() {
    try (final ServerSocketChannel channel = ServerSocketChannel.open()) {
      System.out.println(channel.isBlocking());
      channel.bind(new InetSocketAddress("localhost", 8888));
      final Stopwatch stopwatch = Stopwatch.createStarted();
      try (final SocketChannel socketChannel = channel.accept()) {}
      System.out.println(stopwatch.stop());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case2NoBlockServer() {
    try (final ServerSocketChannel channel = ServerSocketChannel.open()) {
      System.out.println(channel.isBlocking());
      channel.configureBlocking(false);
      System.out.println(channel.isBlocking());
      channel.bind(new InetSocketAddress("localhost", 8888));
      final Stopwatch stopwatch = Stopwatch.createStarted();
      try (final SocketChannel socketChannel = channel.accept()) {
        System.out.println("socketChannel=" + socketChannel);
      }
      System.out.println(stopwatch.stop());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case3Server() {
    try (final ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8888));
      try (final SocketChannel socketChannel = channel.accept()) {
        final ByteBuffer buffer = ByteBuffer.allocate(2);
        int readLength;
        while ((readLength = socketChannel.read(buffer)) != -1) {
          System.out.println(new String(buffer.array()));
          buffer.flip();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    try (final Socket socket = new Socket("localhost", 8888)) {
      final OutputStream outputStream = socket.getOutputStream();
      outputStream.write("我是发送的数据Client".getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
