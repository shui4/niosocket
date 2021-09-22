package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 7. 阻塞在 select() 或 select(long) 方法中的线程调用 interrupt() 方法被中断
 *
 * @author shui4
 * @since 1.0
 */
public class Test7 {
  public static void main(String[] args) {
    final Thread mainThread = Thread.currentThread();
    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open()) {
      serverSocketChannel.bind(new InetSocketAddress(7777));
      serverSocketChannel.configureBlocking(false);
      final SelectionKey selectionKey =
          serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

      new Thread(
              () -> {
                try {
                  TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "中断了" + mainThread.getName());
                mainThread.interrupt();
              },
              "client")
          .start();

      while (true) {
        System.out.println("begin " + System.currentTimeMillis());
        final int keyCount = selector.select();
        // 清除中断状态，继续无限循环运行
        mainThread.interrupt();
        System.out.println("  end" + System.currentTimeMillis());

        final Set<SelectionKey> keys = selector.keys();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            final ServerSocket socket = channel.socket();
            socket.close();
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
