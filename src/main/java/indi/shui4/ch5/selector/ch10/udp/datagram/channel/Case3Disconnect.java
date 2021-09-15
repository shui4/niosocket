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
 * 5.10.3 断开连接 {@link DatagramChannel#disconnect()}
 *
 * @author shui4
 * @since 1.0
 */
public class Case3Disconnect {
  /** 使用 {@link Case1UseDatagramChannelImplUDP#server()} */
  @Test
  public void server() {
    new Case1UseDatagramChannelImplUDP().server();
  }
  // NotYetConnectedException
  @Test
  public void client() {
    try (Selector selector = Selector.open();
        DatagramChannel datagramChannel = DatagramChannel.open()) {
      datagramChannel.configureBlocking(false);
      datagramChannel.connect(new InetSocketAddress("localhost", 8888));
      datagramChannel.disconnect();
      datagramChannel.register(selector, SelectionKey.OP_WRITE);
      selector.select();
      final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        iterator.remove();
        if (key.isWritable()) {
          final ByteBuffer buffer = ByteBuffer.wrap("我来自客户端！".getBytes());
          datagramChannel.write(buffer);
          datagramChannel.close();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
