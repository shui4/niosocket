package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import indi.shui4.util.SocketChannelUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 1. Selection Key 执行 cancel()方法后的效果 <br>
 *
 * @author shui4
 * @since 1.0
 */
public class Test1 {

  @Test
  public void case1() {

    try (final Selector selector = Selector.open();
         final ServerSocketChannel serverSocketChannel1 =
            SocketChannelUtil.getServerSocketChannel(7777, selector);
         final ServerSocketChannel serverSocketChannel2 =
            SocketChannelUtil.getServerSocketChannel(8888, selector); ) {
      new Thread(
              () -> {
                try (Socket socket = new Socket("localhost", 7777)) {
                  socket.getOutputStream().write("我是中国人，我来自客户端 to7777!".getBytes());
                } catch (IOException e) {
                  e.printStackTrace();
                }

                try (Socket socket = new Socket("localhost", 8888)) {
                  socket.getOutputStream().write("我是中国人，我来自客户端 to8888!".getBytes());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              },
              "client")
          .start();

      new Thread(
              () -> {
                try {
                  TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                System.out.println();
                final Set<SelectionKey> keysSet = selector.keys();
                final Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                System.out.println("select()方法执行第2次后的信息：");
                System.out.println("keysSet.size()=" + keysSet.size());
                System.out.println("selectionKeySet.size()=" + selectionKeySet.size());
              },
              "info")
          .start();

      while (true) {
        final int keyCount = selector.select();
        final Set<SelectionKey> keysSet = selector.keys();
        final Set<SelectionKey> selectionKeySet = selector.selectedKeys();
        System.out.println("取消之前的信息：");
        System.out.println("keysSet.size()=" + keysSet.size());
        System.out.println("selectionKeySet.size()=" + selectionKeySet.size());
        System.out.println();
        final Iterator<SelectionKey> iterator = selectionKeySet.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          if (key.isAcceptable()) {
            final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            final ServerSocket serverSocket = serverSocketChannel.socket();

            try (final Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream()) {
              final byte[] bytes = new byte[1000];
              int readLength;
              while ((readLength = inputStream.read(bytes)) != -1) {
                final String newString = new String(bytes, 0, readLength);
                System.out.println(newString);
              }

              if (serverSocket.getLocalPort() == 7777) {
                key.cancel();
                System.out.println("取消之后的信息：");
                System.out.println("keysSet.size()=" + keysSet.size());
                System.out.println("selectionKeySet.size()=" + keysSet.size());
              }
            }
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 自定义示例 */
  static class CustomExample {

    @Test
    public void client() {
      try (Socket socket = new Socket("localhost", 8888)) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void server() {
      try (Selector selector = Selector.open();
          ServerSocketChannel serverSocketChannel =
              SocketChannelUtil.getServerSocketChannel(8888, selector)) {
        while (true) {
          selector.select();
          final Set<SelectionKey> keySet = selector.keys();
          final Set<SelectionKey> selectionKeySet = selector.selectedKeys();
          final Iterator<SelectionKey> iterator = selectionKeySet.iterator();
          while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isAcceptable()) {
              final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
              final SocketChannel socketChannel = channel.accept();
              socketChannel.configureBlocking(false);
              socketChannel.register(selector, SelectionKey.OP_READ);
            }
            //            key.cancel();
            if (key.isReadable()) {
              try (SocketChannel channel = (SocketChannel) key.channel()) {
                channel.read(ByteBuffer.allocate(10));
                System.out.println(((InetSocketAddress) channel.getRemoteAddress()).getPort());
              }
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
