package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 5.7.12 获得支持的 SocketOption 列表
 *
 * @author shui4
 * @since 1.0
 */
public class Case12GetSupportedSocketOptionList {
  public static void main(String[] args) {
    new Thread(
            () -> {
              try {
                Thread.sleep(2_000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              try (Socket socket = new Socket("localhost", 8088)) {
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(8088));
      final SocketChannel socketChannel = channel.accept();
      System.out.println("ServerSocketChannel supportedOptions:");
      traverse(channel.supportedOptions());
      System.out.println();
      System.out.println();
      System.out.println("SocketChannel supportedOptions:");
      traverse(socketChannel.supportedOptions());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void traverse(final Set<SocketOption<?>> set2) {
    for (final SocketOption<?> socketOption : set2) {
      System.out.println(socketOption.name() + " " + socketOption.getClass().getName());
    }
  }
}
