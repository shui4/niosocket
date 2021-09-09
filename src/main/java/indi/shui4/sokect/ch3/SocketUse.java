package indi.shui4.sokect.ch3;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * 4.3 Socket 类的使用
 *
 * @author shui4
 * @since 1.0
 */
public class SocketUse {

  /** 4.3.1 绑定 bind 与 connect 以及端口生成的时机 */
  static class Case1BindAndConnectAndPortGenerateOpportunity {

    static class Case1 {
      @Test
      public void server() {

        try (final ServerSocket serverSocket = new ServerSocket(8888);
            final Socket socket = serverSocket.accept()) {
          Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("server end!");
      }

      @Test
      public void client() {
        try (Socket socket = new Socket()) {
          socket.bind(new InetSocketAddress("localhost", 7777));
          socket.connect(new InetSocketAddress("localhost", 8888));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    static class Case2 {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8088)) {
          TimeUnit.SECONDS.sleep(10);
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket()) {
          System.out.println("A=" + socket.getPort());
          socket.connect(new InetSocketAddress("localhost", 8088));
          System.out.println("B=" + socket.getPort());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /** 4.3.2 连接与超时 {@link Socket#connect(java.net.SocketAddress, int)} */
  static class Case2ConnectAndTimeout {
    @Test
    public void client1() {
      try (final Socket socket = new Socket()) {
        socket.bind(new InetSocketAddress("192.168.0.101", 6666));
        final Stopwatch stopwatch = Stopwatch.createStarted();
        socket.connect(new InetSocketAddress("1.1.1.1", 8880), 6_000);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client2() {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      try (final Socket socket = new Socket()) {
        //        socket.connect(new InetSocketAddress("1.1.1.1", 8888), 6_000);
        // 在Windows系统中默认超时20S
        socket.connect(new InetSocketAddress("1.1.1.1", 8888));
      } catch (IOException e) {
        //        e.printStackTrace();
        System.out.println(e.getMessage());
        stopwatch.stop();
        System.out.println(stopwatch);
      }
    }
  }

  /** 4.3.3 获得远程端口与本地端口 */
  static class Case3GetRemotePortAndLocalPort {
    @Test
    public void server() {

      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept()) {
        System.out.println("服务端的输出：");
        System.out.println("服务端的端口号serverSocket.getLocalPort()=" + serverSocket.getLocalPort());
        System.out.println("客户端的端口号socket.getPort()=" + socket.getPort());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket()) {
        socket.bind(new InetSocketAddress("localhost", 7777));
        socket.connect(new InetSocketAddress("localhost", 8888));

        System.out.println("客户端的输出：");
        System.out.println("客户端的端口号=" + socket.getLocalPort());
        System.out.println("服务端的端口号=" + socket.getPort());

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.3.4 获得本地 InetAddress 地址与本地 SocketAddress地址 */
  static class Case4InetAddressAndSocketAddress {
    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept()) {
        final InetAddress inetAddress = socket.getLocalAddress();
        final InetSocketAddress inetSocketAddress =
            (InetSocketAddress) socket.getLocalSocketAddress();
        System.out.print("服务端的IP地址为：");
        final byte[] addressByteArray = inetAddress.getAddress();
        for (final byte b : addressByteArray) {
          System.out.print(b + " ");
        }
        System.out.println();

        System.out.println("服务端的端口为：" + inetSocketAddress.getPort());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket("localhost", 8888)) {
        /* final InetAddress inetAddress = socket.getLocalAddress();
        final InetSocketAddress inetSocketAddress =
            (InetSocketAddress) socket.getLocalSocketAddress();
        System.out.print("服务端的IP地址为：");
        final byte[] addressByteArray = inetAddress.getAddress();
        for (final byte b : addressByteArray) {
          System.out.print(b + " ");
        }
        System.out.println();

        System.out.println("客户端的端口为：" + inetSocketAddress.getPort());*/
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.3.5 获得远程 InetAddress 与远程 SocketAddress地址 */
  static class Case5GetRemoteInetAddressAndRemoteSocketAddress {
    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept()) {
        final InetAddress inetAddress = socket.getInetAddress();
        final InetSocketAddress inetSocketAddress =
            (InetSocketAddress) socket.getRemoteSocketAddress();
        final byte[] byteArray = inetAddress.getAddress();
        System.out.print("客户端的IP地址为：");
        for (final byte b : byteArray) {
          System.out.print(b + " ");
        }
        System.out.println();
        System.out.println("客户端的端口为：" + inetSocketAddress.getPort());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket("localhost", 8888)) {
        final InetAddress inetAddress = socket.getInetAddress();
        final InetSocketAddress inetSocketAddress =
            (InetSocketAddress) socket.getRemoteSocketAddress();
        final byte[] byteArray = inetAddress.getAddress();
        System.out.print("客户端的IP地址为：");
        for (final byte b : byteArray) {
          System.out.print(b + " ");
        }
        System.out.println();
        System.out.println("客户端的端口为：" + inetSocketAddress.getPort());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
