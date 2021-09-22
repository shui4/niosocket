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
 * 5.10.2 连接操作
 *
 * @author shui4
 * @since 1.0
 */
public class Case2Connection {
  /** 使用 {@link Case1UseDatagramChannelImplUDP#server()} */
  @Test
  public void server() {}

  @Test
  public void client() {
    try (DatagramChannel datagramChannel = DatagramChannel.open()) {
      datagramChannel.configureBlocking(false);
      datagramChannel.connect(new InetSocketAddress("localhost", 8888));
      final Selector selector = Selector.open();
      datagramChannel.register(selector, SelectionKey.OP_WRITE);
      selector.select();
      final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        iterator.remove();
        if (key.isWritable()) {
          final ByteBuffer buffer = ByteBuffer.wrap("我来自客户端!".getBytes());
          datagramChannel.write(buffer);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("client end!");
  }
}
