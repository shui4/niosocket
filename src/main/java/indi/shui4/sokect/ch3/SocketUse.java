package indi.shui4.sokect.ch3;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import static indi.shui4.util.CommonConstant.IP;

/**
 * 4.3 Socket 类的使用
 *
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("all")
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
      Stopwatch stopwatch = null;
      try (final Socket socket = new Socket()) {
        socket.bind(new InetSocketAddress("192.168.7.239", 6666));
        stopwatch = Stopwatch.createStarted();
        socket.connect(new InetSocketAddress("1.1.1.1", 8880), 6_000);
      } catch (IOException e) {
        if (e instanceof SocketTimeoutException) {
          System.out.println(stopwatch.stop());
        }
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
        System.out.print("服务端的IP地址为：");
        for (final byte b : byteArray) {
          System.out.print(b + " ");
        }
        System.out.println();
        System.out.println("服务端的端口为：" + inetSocketAddress.getPort());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.3.6 套接字状态的判断 */
  static class Case6SocketCStatusJudge {

    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept()) {

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try {
        final Socket socket = new Socket();
        System.out.println("1 socket.isBound()" + socket.isBound());
        socket.bind(new InetSocketAddress("localhost", 7777));
        System.out.println("2 socket.isBound()" + socket.isBound());
        System.out.println("3 socket.isConnected()" + socket.isConnected());
        socket.connect(new InetSocketAddress("localhost", 8888));
        System.out.println("4 socket.isConnected()" + socket.isConnected());
        System.out.println("5 socket.isClosed()" + socket.isClosed());
        socket.close();
        System.out.println("6 socket.isClosed()" + socket.isClosed());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.3.7 开启半读与半写状态 {@link Socket#shutdownInput()} 和 {@link Socket#shutdownOutput()} */
  static class Case7OpenHalfReadHalfWriteStatus {
    static class Case1ShutdownInput {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8088);
            final Socket socket = serverSocket.accept()) {
          final InputStream inputStream = socket.getInputStream();
          System.out.println("A=" + inputStream.available());
          final byte[] bytes = new byte[2];
          int readLength = inputStream.read(bytes);
          System.out.println("server取得的数据：" + new String(bytes, 0, readLength));
          socket.shutdownInput();
          System.out.println("B=" + inputStream.available());
          readLength = inputStream.read(bytes);
          System.out.println("readLength=" + readLength);
          // 再次调用 getInputStream 方法出现异常： SocketException
          socket.getInputStream();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket("localhost", 8088);
            final OutputStream outputStream = socket.getOutputStream(); ) {
          outputStream.write("123".getBytes());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    static class Case2ShutdownOutput {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8088);
            final Socket socket = serverSocket.accept();
            final OutputStream outputStream = socket.getOutputStream()) {
          outputStream.write("123".getBytes());
          socket.shutdownOutput();
          TimeUnit.SECONDS.sleep(10);
          // 出现异常
          socket.getOutputStream();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket("localhost", 8088);
            final InputStream inputStream = socket.getInputStream()) {
          final byte[] bytes = new byte[100];
          int readLength;
          Stopwatch started = Stopwatch.createStarted();
          while ((readLength = inputStream.read(bytes)) != -1) {
            System.out.print(new String(bytes, 0, readLength));
          }
          System.out.println();
          System.out.println(started.stop());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /** 4.3.8 判断半读半写状态 */
  static class Case8JudgeHalfReadAndHalfWrite {
    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket(8088);
          final Socket socket = serverSocket.accept()) {
        System.out.println("A socket.isOutputShutdown()=" + socket.isOutputShutdown());
        socket.shutdownOutput();
        System.out.println("B socket.isOutputShutdown()=" + socket.isOutputShutdown());
        System.out.println();
        System.out.println("C socket.isInputShutdown()=" + socket.isInputShutdown());
        socket.shutdownInput();
        System.out.println("D socket.isInputShutdown()=" + socket.isInputShutdown());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket("localhost", 8088)) {

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 4.3.9 Socket选项TcpNoDelay<br>
   * 这里建议在2台计算机上分别运行server和client方法，来对比延时性
   */
  static class Case9SocketOptionsTcpNoDelay {

    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept()) {
        System.out.println("A=" + socket.getTcpNoDelay());
        // 立即发送，不缓存数据，不启用Nagle算法
        socket.setTcpNoDelay(true);
        System.out.println("B=" + socket.getTcpNoDelay());
        OutputStream outputStream = socket.getOutputStream();
        for (int i = 0; i < 10; i++) {
          outputStream.write("abcdefg".getBytes());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void server2() {
      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept()) {
        System.out.println("A=" + socket.getTcpNoDelay());
        // 缓存数据
        socket.setTcpNoDelay(false);
        System.out.println("B=" + socket.getTcpNoDelay());
        OutputStream outputStream = socket.getOutputStream();
        for (int i = 0; i < 10; i++) {
          outputStream.write("abcdefg".getBytes());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket("localhost", 8888)) {
        socket.setTcpNoDelay(false);
        InputStream inputStream = socket.getInputStream();
        final byte[] bytes = new byte[1];
        final Stopwatch stopwatch = Stopwatch.createStarted();
        int readLength;
        while ((readLength = inputStream.read(bytes)) != -1) {
          final String newString = new String(bytes, 0, readLength);
          System.out.println(newString);
        }
        stopwatch.stop();
        System.out.println(stopwatch);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.3.10 Socket 选项 SendBufferSize */
  static class Case10SocketOptionSendBufferSize {
    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket()) {
        serverSocket.setReceiveBufferSize(1);
        serverSocket.bind(new InetSocketAddress(8888));
        final Socket socket = serverSocket.accept();
        final InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        final char[] chars = new char[1024];
        int readLength;
        final Stopwatch stopwatch = Stopwatch.createStarted();
        while ((readLength = reader.read(chars)) != -1) {
          System.out.println(new String(chars, 0, readLength));
        }
        stopwatch.stop();
        System.out.println(stopwatch);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket()) {
        System.out.println("A client socket.getSendBufferSize()=" + socket.getSendBufferSize());
        socket.setSendBufferSize(1);
        //        socket.setSendBufferSize(1024*1024);
        System.out.println("B client socket.getSendBufferSize()=" + socket.getSendBufferSize());
        socket.connect(new InetSocketAddress("localhost", 8888));
        final OutputStream outputStream = socket.getOutputStream();
        final Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 5_0000; i++) {
          outputStream.write("123456789123456789123456789123456789123456789".getBytes());
          System.out.println(i + 1);
        }
        System.out.println("写耗时：" + stopwatch.stop());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.3.11 Socket 选项 Linger */
  static class Case11SocketOptionLinger {
    /** 1.验证：在on=true、linger=0时，close（）方法立即返回且丢弃数据，并且发送RST标记 */
    static class Case1 {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8088);
            final Socket socket = serverSocket.accept()) {
          System.out.println("socket.getSoLinger()=" + socket.getSoLinger());
          socket.setSoLinger(true, 0);
          System.out.println("socket.getSoLinger()=" + socket.getSoLinger());
          final OutputStream outputStream = socket.getOutputStream();
          for (int i = 0; i < 10; i++) {
            outputStream.write(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    .getBytes());
          }
          outputStream.write("end!".getBytes());
          final Stopwatch stopwatch = Stopwatch.createStarted();
          outputStream.close();
          stopwatch.stop();
          System.out.println(stopwatch);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket()) {
          socket.setReceiveBufferSize(1);
          socket.connect(new InetSocketAddress("localhost", 8088));
          final InputStream inputStream = socket.getInputStream();
          final byte[] bytes = new byte[1];
          int readLength;
          // java.net.SocketException: Connection reset，
          // 由于服务端关闭直接将缓冲区数据丢弃，因此无法获取，导致数据不完整
          while ((readLength = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, readLength));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    /** 2.验证：在on=false时，close（）方法立即返回并且数据不丢失，正常进行4次“挥手” */
    static class Case2 {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8088)) {
          final Socket socket = serverSocket.accept();
          System.out.println("A socket.getSoLinger()=" + socket.getSoLinger());
          final int lingerSeconds = 123123;
          socket.setSoLinger(false, lingerSeconds);
          System.out.println("B socket.getSoLinger()=" + socket.getSoLinger());
          final OutputStream outputStream = socket.getOutputStream();
          for (int i = 0; i < 10; i++) {
            outputStream.write(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    .getBytes());
          }
          outputStream.write("end!".getBytes());
          System.out.println("socket close before=" + System.currentTimeMillis());
          outputStream.close();
          System.out.println("socket close end=" + System.currentTimeMillis());
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket()) {
          // 设置超小的接收缓冲区
          // 目的是先让Server执行close()
          // 然后将server发送缓冲区中的数据再传入客户端的接收缓冲区中
          // 虽然服务端的socket.close()已经执行，但是数据不会丢失
          socket.setReceiveBufferSize(1);
          socket.bind(new InetSocketAddress("localhost", 7070));
          socket.connect(new InetSocketAddress("localhost", 8088));
          final InputStream inputStream = socket.getInputStream();
          final byte[] bytes = new byte[1];
          int readLength;
          while ((readLength = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, readLength));
          }
          System.out.println("client read end time=" + System.currentTimeMillis());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    /** 3.验证：如果只是调用close()方法，则立即返回并且数据不丢失，正常进行4次“挥手” */
    static class Case3 {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8088);
            final Socket socket = serverSocket.accept()) {
          System.out.println("A socket.getSoLinger()=" + socket.getSoLinger());
          System.out.println("B socket.getSoLinger()=" + socket.getSoLinger());
          final OutputStream outputStream = socket.getOutputStream();
          for (int i = 0; i < 10; i++) {
            outputStream.write(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    .getBytes());
          }
          outputStream.write("end!".getBytes());
          System.out.println("socket close before=" + System.currentTimeMillis());
          outputStream.close();
          System.out.println("socket close end=" + System.currentTimeMillis());
          socket.close();

        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket()) {
          // 设置超小的接收缓冲区
          // 目的是先让Server执行close()
          // 然后将server发送缓冲区中的数据再传入客户端的接收缓冲区中
          // 虽然服务端的socket.close()已经执行，但是数据不会丢失
          socket.setReceiveBufferSize(1);
          socket.bind(new InetSocketAddress("localhost", 7070));
          socket.connect(new InetSocketAddress("localhost", 8088));
          final InputStream inputStream = socket.getInputStream();
          final byte[] bytes = new byte[1];
          int readLength;
          while ((readLength = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, readLength));
          }
          System.out.println("client read end time=" + System.currentTimeMillis());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    /** 4.测试：在on=true、linger=10时，发送数据耗时小于10s的情况 */
    /** 5.测试：在on=true、linger=10时，发送数据耗时大于10s的情况 */
    static class Case4 {
      @Test
      public void server() {
        // 这种会超过10s
        //                final int capacity = 1_000_000*15;
        final int capacity = 1_000_000;
        final StringBuilder stringBuilder = new StringBuilder(capacity);
        for (int i = 0; i < capacity; i++) {
          stringBuilder.append("1");
        }
        try (final ServerSocket serverSocket = new ServerSocket(8088)) {
          final Socket socket = serverSocket.accept();
          socket.setSendBufferSize(capacity);
          socket.setSoLinger(true, 10);
          final OutputStream outputStream = socket.getOutputStream();
          outputStream.write(stringBuilder.toString().getBytes());
          final Stopwatch stopwatch = Stopwatch.createStarted();
          System.out.println("C=" + System.currentTimeMillis());
          socket.close();
          System.out.println("D=" + System.currentTimeMillis());
          System.out.println("时间差：" + stopwatch.stop());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket("localhost", 8088)) {
          final InputStream inputStream = socket.getInputStream();
          final byte[] bytes = new byte[1];
          int readLength;
          while ((readLength = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, readLength));
          }
          System.out.println("E=" + System.currentTimeMillis());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /** 4.3.12 Socket 选项 Timeout */
  static class Case12SocketOptionTimeout {

    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept()) {
        System.out.println("setSoTimeout before:" + socket.getSoLinger());
        socket.setSoTimeout(5_000);
        System.out.println("setSoTimeout after:" + socket.getSoLinger());
        final InputStream inputStream = socket.getInputStream();
        final byte[] bytes = new byte[1024];
        System.out.println("read begin__:" + System.currentTimeMillis());
        inputStream.read(bytes);
        System.out.println("read end:" + System.currentTimeMillis());
      } catch (IOException e) {
        if (e instanceof SocketTimeoutException) {
          System.out.println("timeout time:" + System.currentTimeMillis());
        }
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket("localhost", 8_888)) {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.3.13 Socket 选项 OOBInline */
  static class Case13SocketOptionOOBInline {
    /** OOBInline设置为true，接收紧急数据 */
    static class Case1 {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8808);
            final Socket socket = serverSocket.accept()) {
          System.out.println("server A getOOBInline=" + socket.getOOBInline());
          socket.setOOBInline(true);
          System.out.println("server B getOOBInline=" + socket.getOOBInline());
          final InputStreamReader reader = new InputStreamReader(socket.getInputStream());
          final char[] charBuffer = new char[1024];
          int readLength;
          while ((readLength = reader.read(charBuffer)) != -1) {
            System.out.println(new String(charBuffer, 0, readLength));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket("localhost", 8808)) {
          final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
          socket.sendUrgentData((byte) 'a');
          writer.write("zzzzzzzzzzzzzzzzzzzzzzz");
          socket.sendUrgentData((byte) 'b');
          socket.sendUrgentData((byte) 'c');
          // 必须flush()，不然不会出现预期的效果：write中的数据需要 flush()之后才会发送到server，你可以在这里 flush()设置断点
          writer.flush();
          socket.sendUrgentData((byte) 'd');
          // 这里测试 flush之后才会发送 到 server  writer.write(...) 的数据
          /*TimeUnit.SECONDS.sleep(10);
          writer.flush();*/

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    /** OOBInline设置为false（默认），丢弃紧急数据 */
    static class Case2 {
      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8808);
            final Socket socket = serverSocket.accept()) {
          System.out.println("server A getOOBInline=" + socket.getOOBInline());
          socket.setOOBInline(false);
          System.out.println("server B getOOBInline=" + socket.getOOBInline());
          final InputStreamReader reader = new InputStreamReader(socket.getInputStream());
          final char[] charBuffer = new char[1024];
          int readLength;
          while ((readLength = reader.read(charBuffer)) != -1) {
            System.out.println(new String(charBuffer, 0, readLength));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket("localhost", 8808)) {
          final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
          socket.sendUrgentData((byte) 'a');
          writer.write("zzzzzzzzzzzzzzzzzzzzzzz");
          socket.sendUrgentData((byte) 'b');
          socket.sendUrgentData((byte) 'c');
          // 必须flush()，不然不会出现预期的效果：write中的数据需要 flush()之后才会发送到server，你可以在这里 flush()设置断点
          writer.flush();
          socket.sendUrgentData((byte) 'd');
          // 这里测试 flush之后才会发送 到 server  writer.write(...) 的数据
          /*TimeUnit.SECONDS.sleep(10);
          writer.flush();*/

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    /*使用丢弃紧急数据特性 测试网络连接状态心跳机制*/
    /**
     * 在Windows10中执行方法 sendUrgentData() 到17次出现异常： <br>
     * Connection reset by peer:send <br>
     * 异常起因是windows10服务器发给客户端一个RST导致的，<br>
     * 所以Server在本实验中要放到Linux操作系统中，Client放在windows10中进行测试
     */
    static class Case3 {

      @Test
      public void server() {
        try (final ServerSocket serverSocket = new ServerSocket(8808);
            final Socket socket = serverSocket.accept()) {
          try {
            Thread.sleep(Integer.MAX_VALUE);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Test
      public void client() {
        try (final Socket socket = new Socket("localhost", 8808)) {
          int count = 0;
          while (true) {
            socket.sendUrgentData(1);
            count++;
            System.out.println("执行了 " + count);
            System.out.println("执行了 " + count);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /** 4.3.14 Socket 选项 KeepAlive */
  static class Case14SocketOptionKeepAlive {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket(8888)) {
        System.out.println("server begin");
        final Socket socket = serverSocket.accept();
        System.out.println("server end");
        Thread.sleep(Integer.MAX_VALUE);
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      System.out.println("client begin");
      try (Socket socket = new Socket("localhost", 8888)) {
        System.out.println("a=" + socket.getKeepAlive());
        socket.setKeepAlive(true);

        System.out.println("b=" + socket.getKeepAlive());
        System.out.println("client end");
        try {
          Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /** 4.3.15 Socket 选项 TrafficClass */
  static class Case15SocketOptionTrafficClass {
    @Test
    public void server() {
      try (final ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept(); ) {
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket()) {
        socket.setTrafficClass(1);
        socket.connect(new InetSocketAddress("localhost", 8888));
        socket.getOutputStream().write("我是发送的数据！".getBytes());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
