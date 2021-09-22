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
 * 5.9.3 判断是否已准备好进行写入
 *
 * @author shui4
 * @since 1.0
 */
public class Case3IsWriteOKJudging {
  @Test
  public void server() {

    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(8888));
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

      SocketChannel socketChannel = null;
      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
          }

          if (key.isReadable()) {
            System.out.println("server isReadable()");
            ByteBuffer buffer = ByteBuffer.allocate(1_000);
            int readLength;
            while ((readLength = socketChannel.read(buffer)) != -1) {
              System.out.println(new String(buffer.array(), 0, readLength));
            }
            socketChannel.close();
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    try (Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, SelectionKey.OP_CONNECT);
      socketChannel.connect(new InetSocketAddress(8888));
      while (true) {
        selector.select();
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isConnectable()) {
            System.out.println("client isConnectable()");
            if (socketChannel.isConnectionPending()) {
              while (!socketChannel.finishConnect()) {
                System.out.println("!!socketChannel.finishConnect() ----------");
              }
              socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
          }

          if (key.isWritable()) {
            System.out.println("client isWritable()");
            socketChannel.write(ByteBuffer.wrap("我来自客户端，你好，服务端！".getBytes()));
            socketChannel.close();
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
