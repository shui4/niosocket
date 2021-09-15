package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 5.9.6 设置 attachment 附件
 *
 * @author shui4
 * @since 1.0
 */
public class Case6SetAttachment {
  /** 使用 {@link Case5RegisterIncomingAttachment#server()} */
  @Test
  public void server() {}

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
            if (socketChannel.isConnectionPending()) {
              while (!socketChannel.finishConnect()) {}
            }
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            key.attach("你使用attach(Object)进行注册，我来自客户端，你好服务端！");
            System.out.println("socketChannel==key.channel()=" + (socketChannel == key.channel()));
          }

          if (key.isWritable()) {
            ByteBuffer buffer = ByteBuffer.wrap(((String) key.attachment()).getBytes());
            socketChannel.write(buffer);
            System.out.println(
                "isWritable   --- socketChannel==key.channel()="
                    + (socketChannel == key.channel()));
            socketChannel.close();
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
