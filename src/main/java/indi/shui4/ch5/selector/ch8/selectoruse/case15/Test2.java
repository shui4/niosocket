package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import indi.shui4.util.SocketChannelUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 2. 对通道执行 close()方法后的效果
 *
 * @author shui4
 * @since 1.0
 */
public class Test2 {

  public static void main(String[] args) {
    try (Selector selector = Selector.open();
        final ServerSocketChannel serverSocketChannel1 =
            SocketChannelUtil.getServerSocketChannel(7777, selector);
        final ServerSocketChannel serverSocketChannel2 =
            SocketChannelUtil.getServerSocketChannel(8888, selector)) {

      new Thread(
              () -> {
                try (Socket socket = new Socket("localhost", 7777)) {
                  socket.getOutputStream().write("我来自客户端to7777".getBytes());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              },
              "client")
          .start();

      new Thread(
              () -> {
                while (true) {
                  try {
                    TimeUnit.SECONDS.sleep(10);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  System.out.println();
                  final Set<SelectionKey> keys = selector.keys();
                  final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                  System.out.println("channel.close()之后的信息：");
                  System.out.println("keys.size()=" + keys.size());
                  System.out.println("selectionKeys.size()=" + selectionKeys.size());
                }
              },
              "info")
          .start();
      while (true) {
        final int keyCount = selector.select();
        final Set<SelectionKey> keys = selector.keys();
        final Set<SelectionKey> selectionKeys = selector.selectedKeys();
        System.out.println("channel.close()之前的信息：");
        System.out.println("keys.size()=" + keys.size());
        System.out.println("selectionKeys.size()=" + selectionKeys.size());

        final Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            final ServerSocket serverSocket = channel.socket();
            try (Socket socket = serverSocket.accept()) {
              final byte[] bytes = new byte[1000];
              int readLength;
              InputStream inputStream = socket.getInputStream();
              while ((readLength = inputStream.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, readLength));
              }
              if (serverSocket.getLocalPort() == 7777) {
                // java.nio.channels.spi.AbstractInterruptibleChannel.close方法会调用   key.cancel();
                channel.close();
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    try (Socket socket = new Socket("localhost", 8888)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
