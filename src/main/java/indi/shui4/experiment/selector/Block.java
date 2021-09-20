package indi.shui4.experiment.selector;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("InfiniteLoopStatement")
public class Block {

  @Test
  public void close() throws IOException {
    Selector selector = Selector.open();
    final Thread mainThread = Thread.currentThread();
    new Thread(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(1);
                final Stopwatch stopwatch = Stopwatch.createStarted();
                //                selector.close();
                //                selector.wakeup();
                mainThread.interrupt();
                System.out.println("close ->" + stopwatch.stop());
              } catch (Exception e) {
                e.printStackTrace();
              }
            },
            "close")
        .start();
    selector.select();
  }

  @Test
  public void server() throws IOException, InterruptedException {
    final Selector selector = Selector.open();
    final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress(8088));
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    Runnable runnable =
        () -> {
          try {
            while (true) {
              final Stopwatch stopwatch = Stopwatch.createStarted();
              selector.select();
              System.out.println(stopwatch.stop());
              selector.keys();
              final Set<SelectionKey> selectionKeys = selector.selectedKeys();
              final Iterator<SelectionKey> iterator = selectionKeys.iterator();
              while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                  final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                  final SocketChannel socketChannel = channel.accept();
                  socketChannel.configureBlocking(false);
                  socketChannel.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                  final SocketChannel channel = (SocketChannel) key.channel();
                  final ByteBuffer buffer = ByteBuffer.allocate(8);
                  int readLength;
                  while ((readLength = channel.read(buffer)) != -1) {
                    buffer.flip();
                    System.out.print(new String(buffer.array(), 0, readLength));
                    buffer.clear();
                  }
                  channel.close();
                  System.out.println();
                }
              }
            }

          } catch (IOException e) {
            e.printStackTrace();
          }
        };

    new Thread(runnable, "1").start();
    new Thread(runnable, "2").start();
    Thread.currentThread().join();
  }

  @Test
  public void client() throws IOException {
    for (int i = 0; i < 5; i++) {
      final SocketChannel channel = SocketChannel.open();
      channel.connect(new InetSocketAddress(8088));
      final ByteBuffer buffer = ByteBuffer.wrap("hello server!".getBytes());
      while (buffer.hasRemaining()) {
        channel.write(buffer);
      }
      channel.close();
    }
  }
}
