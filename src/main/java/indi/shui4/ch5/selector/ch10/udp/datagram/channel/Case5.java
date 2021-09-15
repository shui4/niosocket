package indi.shui4.ch5.selector.ch10.udp.datagram.channel;

import indi.shui4.util.CommonConstant;
import jdk.nashorn.internal.runtime.arrays.IteratorAction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static indi.shui4.util.CommonConstant.IP;

/**
 * 5.10.5 将通道加入组播地址旦接收指定客户端数据
 *
 * @author shui4
 * @since 1.0
 */
public class Case5 {

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET)) {
      final InetAddress address = InetAddress.getByName("224.0.0.5");
      final NetworkInterface networkInterface =
          NetworkInterface.getByInetAddress(InetAddress.getByName(IP));
      datagramChannel.join(address, networkInterface);
      datagramChannel.bind(new InetSocketAddress(IP, 8088));
      datagramChannel.configureBlocking(false);
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
            System.out.println(
                new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8));
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
      datagramChannel.bind(new InetSocketAddress(IP, 9090));
      datagramChannel.connect(new InetSocketAddress("224.0.0.5", 8088));
      datagramChannel.configureBlocking(false);
      datagramChannel.register(selector, SelectionKey.OP_WRITE);
      selector.select();
      final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        if (key.isWritable()) {
          datagramChannel.write(ByteBuffer.wrap("form linux!".getBytes()));
          datagramChannel.close();
        }
      }
      System.out.println("client end!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
