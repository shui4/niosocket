package indi.shui4.ch5.selector.ch7;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * 5.7.26 验证read 和 write 方法时阻塞的
 *
 * @author shui4
 * @since 1.0
 */
public class Case26ReadAndWriteMethodIsBlocked {
  /** 非阻塞 read */
  static class NoBlockRead {
    /** 这里read并没有读到数据就继续往下运行，说明read具有非阻塞特性。 */
    @Test
    public void server() {
      try (ServerSocketChannel channel = ServerSocketChannel.open()) {
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(8088));
        final Selector selector = Selector.open();
        channel.register(selector, OP_ACCEPT);
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          final SelectionKey key = iterator.next();
          final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
          final SocketChannel socketChannel = serverSocketChannel.accept();
          socketChannel.configureBlocking(false);
          final ByteBuffer buffer = ByteBuffer.allocate(100);
          System.out.println("begin " + System.currentTimeMillis());
          socketChannel.read(buffer);
          System.out.println("end " + System.currentTimeMillis());
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (SocketChannel socketChannel = SocketChannel.open()) {
        socketChannel.connect(new InetSocketAddress(8088));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 非阻塞 write */
  static class NoBlockWrite {
    /** 方法write并没有将全部的214748364字节传输到对端，只传输了131071个字节，说明write具有非阻塞特性。 */
    @Test
    public void server() {
      try (ServerSocketChannel channel = ServerSocketChannel.open()) {
        // 服务初始化
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(8088));
        final Selector selector = Selector.open();
        channel.register(selector, OP_ACCEPT);

        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
          final SocketChannel socketChannel = serverSocketChannel.accept();
          socketChannel.configureBlocking(false);
          final ByteBuffer buffer = ByteBuffer.allocate(Integer.MAX_VALUE / 10);
          System.out.println("buffer.limit()=" + buffer.limit());
          System.out.println("begin " + System.currentTimeMillis());
          socketChannel.write(buffer);
          System.out.println(
              "end " + System.currentTimeMillis() + " buffer.position()=" + buffer.position());
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (SocketChannel socketChannel = SocketChannel.open()) {
        socketChannel.connect(new InetSocketAddress(8088));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  
  
  
}
