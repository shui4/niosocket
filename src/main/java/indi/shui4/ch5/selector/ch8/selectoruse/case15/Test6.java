package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import indi.shui4.util.SocketChannelUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 6. 阻塞在 select()或 select(long)方法中的线程通过选择器的 close()方法被中断
 *
 * @author shui4
 * @since 1.0
 */
public class Test6 {
  public static void main(String[] args) {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel =
            SocketChannelUtil.getServerSocketChannel(7777, selector)) {
      new Thread(
              () -> {
                try {
                  TimeUnit.SECONDS.sleep(2);
                  try {
                    selector.close();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              },
              "client")
          .start();

      while (true) {
        System.out.println("begin " + System.currentTimeMillis());
        final int keyCount = selector.select();
        System.out.println("  end " + System.currentTimeMillis());
        final Set<SelectionKey> keys = selector.keys();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey next = iterator.next();
          iterator.remove();
          if (next.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) next.channel();
            final ServerSocket serverSocket = channel.socket();
            final Socket socket = serverSocket.accept();
            socket.close();
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
