package indi.shui4.ch5.selector.ch10.udp.datagram.channel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * 5.10.1 使用 DatagramChannel 类实现 UDP 通信
 *
 * @author shui4
 * @since 1.0
 */
public class Case1UseDatagramChannelImplUDP {
  @Test
  public void server() {
    try (Selector selector = Selector.open();
        DatagramChannel datagramChannel = DatagramChannel.open()) {
      datagramChannel.configureBlocking(false);
      datagramChannel.bind(new InetSocketAddress(8888));
      datagramChannel.register(selector, SelectionKey.OP_READ);

      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isReadable()) {
            final DatagramChannel channel = (DatagramChannel) key.channel();
            final ByteBuffer buffer = ByteBuffer.allocate(1000);
            channel.receive(buffer);
            System.out.println(new String(buffer.array(), 0, buffer.position()));
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
        DatagramChannel datagramChannel = DatagramChannel.open()) {
      datagramChannel.configureBlocking(false);
      datagramChannel.register(selector, SelectionKey.OP_WRITE);
      selector.select();
      final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        if (key.isWritable()) {
          final ByteBuffer buffer = ByteBuffer.wrap("我来自客户端！".getBytes());
          final DatagramChannel channel = (DatagramChannel) key.channel();
          channel.send(buffer, new InetSocketAddress("localhost", 8888));
          System.out.println(datagramChannel.isOpen());
          System.out.println("datagramChannel == channel=" + (datagramChannel == channel));
          channel.close();
          System.out.println(datagramChannel.isOpen());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
