package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author shui4
 * @since 1.0
 */
public class Case9GetTheSetOfReadyOperationsForThisKey {
  @Test
  public void client() {
    try (Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, SelectionKey.OP_CONNECT);
      socketChannel.connect(new InetSocketAddress(8888));
      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();

          if (key.isConnectable()) {
            print(key, false);
            while (!socketChannel.finishConnect()) {
              System.out.println("!socketChannel.finishConnect()-----");
            }
            socketChannel.register(selector, SelectionKey.OP_WRITE, "我使用附件进行注册，我来自客户端，你好服务端！");
          }

          if (key.isWritable()) {
            print(key, false);
            final byte[] bytes = ((String) key.attachment()).getBytes();
            socketChannel.write(ByteBuffer.wrap(bytes));
            System.out.println(
                "socketChannel == key.channel()=" + (socketChannel == key.channel()));
            socketChannel.close();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("client end!");
  }

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.bind(new InetSocketAddress(8888));
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            print(key, true);
            final SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
          }
          if (key.isReadable()) {
            print(key, true);
            final SocketChannel channel = (SocketChannel) key.channel();
            final ByteBuffer buffer = ByteBuffer.allocate(1000);
            int readLength;
            while ((readLength = channel.read(buffer)) != -1) {
              System.out.println(new String(buffer.array(), 0, readLength));
            }
            channel.close();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void print(final SelectionKey key, final boolean isServer) {
    if (isServer) {
      System.out.println(
          "server isAcceptable() OP_ACCEPT result=" + (SelectionKey.OP_ACCEPT & ~key.readyOps()));
    }
    System.out.println(
        "server isAcceptable() OP_CONNECT result=" + (SelectionKey.OP_CONNECT & ~key.readyOps()));
    System.out.println(
        "server isAcceptable() OP_READ result=" + (SelectionKey.OP_READ & ~key.readyOps()));
    System.out.println(
        "server isAcceptable() OP_WRITE result=" + (SelectionKey.OP_WRITE & ~key.readyOps()));
  }
}
