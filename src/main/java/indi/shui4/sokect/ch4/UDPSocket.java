package indi.shui4.sokect.ch4;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * 4.4 基于 UDP Socket 通信
 *
 * @author shui4
 * @since 1.0
 */
public class UDPSocket {
  /** 4.4.1 UDP 实现 Socket 通信 */
  static class Case1UseUDPImplSocketCommunicate {

    @Test
    public void client() {
      try (final DatagramSocket socket = new DatagramSocket()) {
        socket.connect(new InetSocketAddress("localhost", 8888));
        final byte[] bytes = "1234567890".getBytes();
        socket.send(new DatagramPacket(bytes, bytes.length));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void server() {
      try (final DatagramSocket socket = new DatagramSocket(8888)) {
        // 构造方法第2个参数也要写上10个，代表要接收数据的长度为10
        // 和客户端发送的长度要保持一致
        final byte[] bytes = new byte[10];
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        final int length = packet.getLength();
        System.out.println("包中的数据长度：" + length);
        System.out.println(new String(packet.getData(), 0, length));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /** 4.4.2 测试发送超大数据量的包导致数据截断的情况 */
  static class Case2SendBigData {
    /** 不报错的情况：没有超过65507 */
    static class Case1 {
      @Test
      public void client() {
        try (final DatagramSocket socket = new DatagramSocket()) {
          socket.connect(new InetSocketAddress("localhost", 8088));
          StringBuilder stringBuilder = new StringBuilder(65507 - 3);
          for (int i = 0; i < 65507 - 3; i++) {
            stringBuilder.append("a");
          }
          final byte[] bytes = stringBuilder.toString().getBytes();
          socket.send(new DatagramPacket(bytes, bytes.length));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void server() {
        try {
          final DatagramSocket socket = new DatagramSocket(8088);
          final int byteLength = 65507;
          final DatagramPacket packet = new DatagramPacket(new byte[byteLength], byteLength);
          socket.receive(packet);
          socket.close();
          System.out.println("服务端收到的数据长度：" + packet.getLength());
          final String strData = new String(packet.getData(), 0, packet.getLength());
          System.out.println(strData);
          final FileOutputStream fileOutputStream =
              new FileOutputStream(FileUtil.getBuildPath("udp-case2-1.txt"));
          fileOutputStream.write(strData.getBytes());
          fileOutputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    /** 超过65507的情况 */
    static class Case2 {
      @Test
      public void client() {
        try (final DatagramSocket socket = new DatagramSocket()) {
          socket.connect(new InetSocketAddress("localhost", 8088));
          StringBuilder stringBuilder = new StringBuilder(65507 - 3);
          for (int i = 0; i < 65507 - 3; i++) {
            stringBuilder.append("a");
          }
          stringBuilder.append("end");
          stringBuilder.append("zz");
          final byte[] bytes = stringBuilder.toString().getBytes();
          socket.send(new DatagramPacket(bytes, bytes.length));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void server() {
        try {
          final DatagramSocket socket = new DatagramSocket(8088);
          final int byteLength = 65509;
          final DatagramPacket packet = new DatagramPacket(new byte[byteLength], byteLength);
          socket.receive(packet);
          socket.close();
          System.out.println("服务端收到的数据长度：" + packet.getLength());
          final String strData = new String(packet.getData(), 0, packet.getLength());
          System.out.println(strData);
          final FileOutputStream fileOutputStream =
              new FileOutputStream(FileUtil.getBuildPath("udp-case2-2.txt"));
          fileOutputStream.write(strData.getBytes());
          fileOutputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  /** 4.4.3 Datagram Packet 类中常用 API 的使用 */
  static class Case3DatagramPacketAPI {
    @Test
    public void client1() {
      try (final DatagramSocket socket = new DatagramSocket()) {
        socket.connect(new InetSocketAddress("localhost", 8888));
        String newString = "我是员工";
        final byte[] byteArray = newString.getBytes();
        final DatagramPacket packet = new DatagramPacket(new byte[] {}, 0);
        packet.setData(byteArray);
        packet.setLength(2);
        socket.send(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client2() {

      try (final DatagramSocket socket = new DatagramSocket()) {
        socket.connect(new InetSocketAddress("localhost", 8888));
        String newString = "我是员工";
        final byte[] byteArray = newString.getBytes(StandardCharsets.UTF_8);
        final DatagramPacket packet = new DatagramPacket(new byte[] {}, 0);
        packet.setData(byteArray, 2, 6);
        System.out.println("packet.getOffset()=" + packet.getOffset());
        socket.send(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void server() {
      try {
        final DatagramSocket socket = new DatagramSocket(8888);
        final byte[] bytes = new byte[10];
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        socket.close();
        final byte[] byteArray = packet.getData();
        System.out.println(new String(byteArray, 0, packet.getLength(), StandardCharsets.UTF_8));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /** 4.4.4 使用 UDP 实现单播 */
  static class Case4Unicast {
    @Test
    public void server() {
      try (final DatagramSocket socket = new DatagramSocket(8888)) {
        byte[] bytes = new byte[10];
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        bytes = packet.getData();
        System.out.println(new String(bytes, 0, packet.getLength()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final DatagramSocket socket = new DatagramSocket()) {
        socket.connect(new InetSocketAddress("192.168.0.150", 8888));
        final byte[] bytes = "1234567890".getBytes();
        socket.send(new DatagramPacket(bytes, bytes.length));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 4.4.5 使用 UDP 实现广播<br>
   * 注意：这里需要选择一个支持广播的地址，{@link InterfaceAddress#getBroadcast()}来获得
   */
  static class Case5Broadcast {
    @Test
    public void server1() {
      try (final DatagramSocket socket = new DatagramSocket(7777)) {
        final byte[] bytes = new byte[10];
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        System.out.println(new String(packet.getData(), 0, packet.getLength()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void server2() {
      try (final DatagramSocket socket = new DatagramSocket(7777)) {
        final byte[] bytes = new byte[10];
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        System.out.println(new String(packet.getData(), 0, packet.getLength()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client1() {
      try (final DatagramSocket socket = new DatagramSocket()) {
        socket.connect(new InetSocketAddress("server1", 7777));
        final byte[] bytes = "12345_____".getBytes();
        socket.send(new DatagramPacket(bytes, bytes.length));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client2() {
      try (final DatagramSocket socket = new DatagramSocket()) {
        socket.connect(new InetSocketAddress("server2", 7777));
        final byte[] bytes = "12345_____".getBytes();
        socket.send(new DatagramPacket(bytes, bytes.length));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /** 4.4.6 使用 UDP 实现组播 */
  static class Multicast {
    // 如果是发送数据报包，则可以不调用joinGroup）方法加入多播组；如果是接收数据报包，则必须调用joinGroup()方法加入多播组。
    @Test
    public void serverA() {
      try (final MulticastSocket socket = new MulticastSocket(8888)) {
        socket.joinGroup(InetAddress.getByName("224.0.0.5"));
        final byte[] bytes = new byte[10];
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        System.out.println(new String(bytes, 0, packet.getLength()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void serverB() {
      try (final MulticastSocket socket = new MulticastSocket(8888)) {
        socket.joinGroup(InetAddress.getByName("224.0.0.5"));
        final byte[] bytes = new byte[10];
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        System.out.println(new String(bytes, 0, packet.getLength()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final MulticastSocket socket = new MulticastSocket()) {
        final byte[] bytes = "1234567890".getBytes();
        final DatagramPacket packet =
            new DatagramPacket(bytes, bytes.length, InetAddress.getByName("224.0.0.5"), 8888);
        socket.send(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
