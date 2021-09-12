package indi.shui4.ch5.selector.ch7;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 5.7.22 完成套接字通道的连接过程 <br>
 * {@link SocketChannel#finishConnect()}
 *
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("DuplicatedCode")
public class Case22DoneSocketChannelConnectionProcedure {

  @Test
  public void connect1() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      final boolean connectResult = socketChannel.connect(new InetSocketAddress("localhost", 8088));
      if (!connectResult) {
        System.out.println("connectResult==false");
        while (!socketChannel.finishConnect()) {
          System.out.println("一直在尝试连接");
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void connect2() {

    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      final boolean connectResult = socketChannel.connect(new InetSocketAddress("localhost", 8088));
      new Thread(
              () -> {
                try {
                  Thread.sleep(50);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                try (ServerSocketChannel channel = ServerSocketChannel.open()) {
                  channel.bind(new InetSocketAddress(8088));
                  channel.accept();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();

      if (!connectResult) {
        System.out.println("connectResult==false");
        while (!socketChannel.finishConnect()) {
          System.out.println("一直在尝试连接");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
