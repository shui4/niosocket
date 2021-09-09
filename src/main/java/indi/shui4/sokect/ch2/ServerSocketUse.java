package indi.shui4.sokect.ch2;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * {@link java.net.ServerSocket} 的使用
 *
 * @author shui4
 * @since 1.0
 */
public class ServerSocketUse {

  /** 4.2.1 接受 accept 与超时 Timeout */
  static class Case1AcceptAndTimeout {
    /** 这里等4s内没有运行，server会出现超时异常 */
    @Test
    public void client() {
      try (Socket socket = new Socket("localhost", 8000)) {
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void server() {
      Stopwatch stopwatch = null;
      try (ServerSocket serverSocket = new ServerSocket(8000)) {
        System.out.println(serverSocket.getSoTimeout());
        serverSocket.setSoTimeout(4_000);
        System.out.println(serverSocket.getSoTimeout());
        System.out.println();
        stopwatch = Stopwatch.createStarted();
        serverSocket.accept();
        stopwatch.stop();
        System.out.println(stopwatch);
      } catch (IOException e) {
        e.printStackTrace();
        if (stopwatch != null) {
          stopwatch.stop();
          System.out.println(stopwatch);
        }
      }
    }
  }

  /**
   * 4.2.2 构造方法的 backlog 参数含义 <br>
   * {@link ServerSocket#ServerSocket(int, int)}
   */
  static class Case2BackLogParams {

    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket(8088, 3)) {
        // sleep(5_000) 的作用是不让ServerSocket调用accept()方法
        // 而是由客户端Socket先发起10个连接请求
        Thread.sleep(5000);
        Socket socket1 = acc(serverSocket, 1);
        Socket socket2 = acc(serverSocket, 2);
        Socket socket3 = acc(serverSocket, 3);
        Socket socket4 = acc(serverSocket, 4);
        Socket socket5 = acc(serverSocket, 5);

        System.out.println();
        socket1.close();
        socket2.close();
        socket3.close();
        socket4.close();
        socket5.close();

      } catch (IOException | InterruptedException e) {

        e.printStackTrace();
      }
    }

    @Test
    public void client() throws IOException {
      for (int i = 0; i < 5; i++) {
        new Socket("localhost", 8088);
      }
    }

    private Socket acc(ServerSocket serverSocket, int id) throws IOException {
      System.out.println("accept" + id + " begin");
      Socket socket = serverSocket.accept();
      System.out.println("accept" + id + " end");

      return socket;
    }
  }

  static class Case3BackLogParamsDefaultValue {
    @Test
    public void server() {
      // 在 ServerSocket没有指定backlog参数的时候，默认50
      try (ServerSocket serverSocket = new ServerSocket(8088)) {
        Thread.sleep(5_000);
        for (int i = 0; i < 100; i++) {
          System.out.println("accept1 begin " + (i + 1));
          serverSocket.accept();
          System.out.println("accept1 end " + (i + 1));
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    /**
     * 达到第51次连接报错
     *
     * @throws IOException the io exception
     */
    @Test
    public void client() throws IOException {
      for (int i = 0; i < 100; i++) {
        Socket socket = new Socket("localhost", 8088);
        System.out.println("client 发起连接次数：" + (i + 1));
      }
    }
  }

  /** {@link ServerSocket#ServerSocket(int, int, java.net.InetAddress)}的使用 */
  static class Case4PortAndBackLogInetAddressParams {
    @Test
    public void server() {
      ServerSocket serverSocket = null;
      try {
        InetAddress inetAddress = InetAddress.getLocalHost();
        serverSocket = new ServerSocket(8088, 50, inetAddress);
        Thread.sleep(5000);
        for (int i = 0; i < 100; i++) {
          serverSocket.accept();
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      } finally {
        if (serverSocket != null) {
          try {
            serverSocket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    @Test
    public void client() throws IOException {
      InetAddress inetAddress = InetAddress.getLocalHost();
      for (int i = 0; i < 100; i++) {
        new Socket(inetAddress, 8088);
        System.out.println("client发起连接次数：" + (i + 1));
      }
    }
  }

  /** {@link ServerSocket#bind(SocketAddress)} */
  static class Case5BindSocket {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket()) {
        serverSocket.bind(new InetSocketAddress(8888));
        Socket socket = serverSocket.accept();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (Socket socket = new Socket("localhost", 8888)) {
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.2.6 绑走到指定的 Socket 地址并设置 backlog 数量 */
  static class Case6BindSocketAndSetBacklog {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket()) {
        serverSocket.bind(new InetSocketAddress(8888), 50);
        Thread.sleep(8000);
        for (int i = 0; i < 100; i++) {
          System.out.println("server accept begin:" + (i + 1));
          serverSocket.accept();
          System.out.println("server accept end:" + (i + 1));
        }

      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() throws IOException {
      for (int i = 0; i < 100; i++) {
        System.out.println("client begin:" + (i + 1));
        Socket socket = new Socket("localhost", 8888);
        System.out.println("client end:" + (i + 1));
      }
    }
  }

  /** 4.2.7 获取本地 SocketAddress 对象以及本地端口 */
  static class Case7GetLocalSocketAddressObjAndLocalPort {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket()) {
        System.out.println("new ServerSocket()无参构造器的端口是" + serverSocket.getLocalPort());
        serverSocket.bind(new InetSocketAddress("192.168.43.123", 8888));
        System.out.println("调用完bind方法之后的端口是：" + serverSocket.getLocalPort());
        InetSocketAddress inetSocketAddress =
            (InetSocketAddress) serverSocket.getLocalSocketAddress();
        System.out.println("inetSocketAddress.getHostName()=" + inetSocketAddress.getHostName());
        System.out.println(
            "inetSocketAddress.getHostString()=" + inetSocketAddress.getHostString());
        System.out.println("inetSocketAddress.getPort()=" + inetSocketAddress.getPort());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /** 4.2.8 {@link InetSocketAddress} 类的使用，请到 {@link indi.shui4.sokect.ch2.InetSocketAddressUse} */
  static class Case8InetSocketAddressUse {}

  /** 4.2.9 关闭与获取关闭状态 */
  static class Case9CloseAndGetCloseStatus {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket(8888)) {
        System.out.println(serverSocket.isClosed());
        serverSocket.close();
        System.out.println(serverSocket.isClosed());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.2.10 判断 Socket 绑定状态 */
  static class Case10JudgeSocketBindStatus {
    @Test
    public void case1() {
      try (ServerSocket serverSocket = new ServerSocket()) {
        System.out.println("bind begin " + serverSocket.isBound());
        serverSocket.bind(new InetSocketAddress(8888));
        System.out.println("bind end " + serverSocket.isBound());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void case2() {
      try (ServerSocket serverSocket = new ServerSocket()) {
        System.out.println("bind begin " + serverSocket.isBound());
        serverSocket.bind(new InetSocketAddress("wwww.baidubaidu.com", 8888));
        System.out.println("bind end " + serverSocket.isBound());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.2.11 获得 IP 地址信息 */
  static class Case11GetIpInfo {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket()) {
        serverSocket.bind(new InetSocketAddress("localhost", 8088));
        InetAddress inetAddress = serverSocket.getInetAddress();
        System.out.println(inetAddress.getHostAddress());
        System.out.println(inetAddress.getHostName());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /**
   * 4.2.12 {@link ServerSocket#setReuseAddress(boolean)} <br>
   * {@link Socket#setReuseAddress(boolean)} <br>
   * 需要在Linux下运行，端口复用Windows不支持端口复用
   */
  static class Case12SocketReuseAddress {
    /**
     * 这里2个server方法依次运行，server()运行等待正常退出之后再运行它，此时端口可能在操作系统还存在占用状态为：TIME_WAIT，下面假设还存在占用<br>
     * 在允许复用的情况下(setReuseAddress(true))，不会发生端口占用的异常，监测到当前端口是占用的但是状态是TIME_WAIT，不会出现异常<br>
     * 在不运行复用的情况下(setReuseAddress(false))，发生端口占用的异常
     */
    @Test
    public void server() throws InterruptedException {
      try (ServerSocket serverSocket = new ServerSocket()) {
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress("localhost", 8088));
        try (Socket socket = serverSocket.accept()) {
          TimeUnit.SECONDS.sleep(3);
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    /** 这里的测试与上面是一样的 */
    @Test
    public void client() {
      try (Socket socket = new Socket()) {
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(7777));
        socket.connect(new InetSocketAddress("localhost", 8088));
        System.out.println("socket.getLocalPort()=" + socket.getLocalPort());
        TimeUnit.SECONDS.sleep(3);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.2.13 Socket 选项 ReceiveBufferSize */
  static class Case13ReceiveBufferSize {
    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket()) {
        System.out.println(
            "A server serverSocket.getReceiveBufferSize()" + serverSocket.getReceiveBufferSize());
        serverSocket.setReceiveBufferSize(66);
        System.out.println(
            "B server serverSocket.getReceiveBufferSize()" + serverSocket.getReceiveBufferSize());

        serverSocket.bind(new InetSocketAddress("localhost", 8088));

        try (final Socket socket = serverSocket.accept()) {
          final InputStreamReader reader = new InputStreamReader(socket.getInputStream());
          int readLength;
          final char[] charBuffer = new char[1024];
          while ((readLength = reader.read(charBuffer)) != -1) {
            System.out.println(new String(charBuffer, 0, readLength));
          }
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (Socket socket = new Socket()) {
        System.out.println("begin " + socket.getReceiveBufferSize());
        socket.connect(new InetSocketAddress("localhost", 8088));
        System.out.println("end " + socket.getReceiveBufferSize());
        final OutputStream outputStream = socket.getOutputStream();
        for (int i = 0; i < 100; i++) {
          outputStream.write(
              ("123456789123456789123456789123456789123456789123456789123456789123456789123456789"
                      + "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789")
                  .getBytes());
        }
        outputStream.write("end!".getBytes());
        socket.shutdownOutput();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  
  
}
