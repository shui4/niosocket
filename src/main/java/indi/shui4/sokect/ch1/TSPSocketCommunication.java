package indi.shui4.sokect.ch1;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Stopwatch;
import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 4.1 基于TCP的Socke通信
 *
 * @author shui4
 * @since 2021/9/8(1.0)
 */
@SuppressWarnings("all")
public class TSPSocketCommunication {
  /** 4.1 .1 验证 {@link ServerSocket#accept()}方法具有阻塞特性 */
  public static class Case1BlockedServerSocketAccept {

    @Test
    public void case1Server() {
      try (ServerSocket socket = new ServerSocket(8088)) {
        System.out.println("server 阻塞开始");
        Stopwatch stopwatch = Stopwatch.createStarted();
        socket.accept();
        stopwatch.stop();
        System.out.println(stopwatch);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void case1Client() {
      System.out.println("client连接准备");
      try (Socket socket = new Socket("localhost", 8088)) {
        System.out.println("client连接结束");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    /** 连接CSDN */
    @Test
    public void case2connectionCSDN() {
      try (Socket socket = new Socket("www.csdn.com", 80)) {
        System.out.println("连接成功");
      } catch (IOException e) {
        System.out.println("连接失败");
        e.printStackTrace();
      }
    }

    /** 连接失败 */
    @Test
    public void case3ConnectionFaild() {
      try (Socket socket = new Socket("www.csdnabc.com", 80)) {
        System.out.println("连接成功");
      } catch (IOException e) {
        System.out.println("连接失败");
        e.printStackTrace();
      }
    }

    /**
     * 实现一个Web服务<br>
     * 这里需要IE浏览器访问
     */
    @Test
    public void case4WebServer() {
      try (ServerSocket serverSocket = new ServerSocket(6666)) {
        Socket socket = serverSocket.accept();

        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream(); ) {
          String content;
          while (StrUtil.isNotBlank((content = reader.readLine()))) {
            System.out.print(content);
          }
          IoUtil.write(outputStream, StandardCharsets.UTF_8, false, "<h1>Hello</h1>");
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.1.2 验证 {@link InputStream#read(byte[])}也具有阻塞特性 */
  public static class Case2BlockedServerSocketRead {
    @Test
    public void server() {
      byte[] byteArray = new byte[1024];
      try (ServerSocket serverSocket = new ServerSocket(8088)) {
        System.out.println("accept begin " + System.currentTimeMillis());
        Socket socket = serverSocket.accept();
        System.out.println("accept end " + System.currentTimeMillis());

        InputStream inputStream = socket.getInputStream();
        Stopwatch stopwatch = Stopwatch.createStarted();
        System.out.println("read begin " + System.currentTimeMillis());
        inputStream.read(byteArray);
        System.out.println("read end " + System.currentTimeMillis());
        System.out.println(stopwatch.stop());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      System.out.println("socket begin " + System.currentTimeMillis());
      try (Socket socket = new Socket("localhost", 8088)) {
        System.out.println("socket end " + System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(10);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.1.3 客户端向服务端传递字符串 */
  public static class Case3ClientSendStrToServer {
    @Test
    public void server() {
      char[] charArray = new char[3];
      try (ServerSocket serverSocket = new ServerSocket(8088)) {
        System.out.println("accept begin " + System.currentTimeMillis());
        Socket socket = serverSocket.accept();
        System.out.println("accept end " + System.currentTimeMillis());
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        System.out.println("read begin " + System.currentTimeMillis());
        int readLength;
        while ((readLength = reader.read(charArray)) != -1) {
          String s = new String(charArray, 0, readLength);
          System.out.println(s);
        }
        System.out.println("read end " + System.currentTimeMillis());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      System.out.println("socket begin " + System.currentTimeMillis());
      try (Socket socket = new Socket("localhost", 8088)) {
        System.out.println("socket end " + System.currentTimeMillis());
        Thread.sleep(3_000);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("我是外星人".getBytes());
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      System.out.println("socket end " + System.currentTimeMillis());
    }
  }

  /**
   * 4.1.4 服务端向客户端传递字符串<br>
   * 对于read方法，它会等待对方调用stream.close() 如果是客户端去读 服务端的内容，此时因服务端没有调用
   * stream.close()一直阻塞，在服务端关闭后，客户端会因为服务端关闭造成异常。 <br>
   * 由于 stream.close()会关闭 socket，你如果还想处理其它工作，可以使用 {@link Socket#shutdownOutput()}
   */
  public static class Case4ServerSendStrToClient {

    @Test
    public void server() {
      try /*(ServerSocket serverSocket = new ServerSocket(8088);
          Socket socket = serverSocket.accept();
          //          OutputStream outputStream = socket.getOutputStream()
          )*/ {
        ServerSocket serverSocket = new ServerSocket(8088);
        Socket socket = serverSocket.accept();
        // 不关闭 outputStream的情况
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("我来server端".getBytes());
        //        socket.shutdownOutput();
        TimeUnit.SECONDS.sleep(10);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      System.out.println("client连接准备=" + System.currentTimeMillis());
      try (Socket socket = new Socket("localhost", 8088)) {
        System.out.println("client连接结束=" + System.currentTimeMillis());
        char[] chars = new char[3];
        try (InputStreamReader reader = new InputStreamReader(socket.getInputStream())) {
          System.out.println("ServerB begin " + System.currentTimeMillis());
          int read;
          while ((read = reader.read(chars)) != -1) {
            System.out.print(new String(chars, 0, read));
          }
        }
        System.out.println();
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println("client连接结束=" + System.currentTimeMillis());
    }
  }

  /** 4.1.5 允许多次调write()方法进行写入操作 */
  public static class Case5CaseAllowMultipleCallsWrite {
    @Test
    public void server() {
      char[] charBuffer = new char[15];
      try (ServerSocket serverSocket = new ServerSocket(8088);
          Socket socket = serverSocket.accept()) {
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        int readLength;
        Stopwatch stopwatch = Stopwatch.createStarted();
        while ((readLength = reader.read(charBuffer)) != -1) {
          System.out.print(new String(charBuffer, 0, readLength));
        }
        System.out.println();
        System.out.println(stopwatch.stop());

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("server运行结束");
    }

    @Test
    public void client() {
      try (Socket socket = new Socket("localhost", 8088)) {
        Thread.sleep(2_000);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("我是xxx1".getBytes());
        Thread.sleep(3_000);
        outputStream.write("我是xxx2".getBytes());
        Thread.sleep(3_000);
        outputStream.write("我是xxx3".getBytes());
        Thread.sleep(3_000);
        outputStream.write("我是xxx4".getBytes());
        Thread.sleep(3_000);
        outputStream.write("我是xxx5".getBytes());
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.1.6 实现服务端与客户端多次的往来通信 */
  public static class Case6ServerClientMutuallyReadWrite {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket(8088);
          Socket socket = serverSocket.accept(); ) {
        // 输入
        final BufferedReader reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s;
        while ((StrUtil.isNotBlank(s = reader.readLine()))) {
          System.out.print(s);
        }
        socket.shutdownInput();
        System.out.println();
        final BufferedWriter writer =
            new BufferedWriter(
                new OutputStreamWriter(new ObjectOutputStream(socket.getOutputStream())));
        write(writer, "客户端");
        writer.close();
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (final Socket socket = new Socket("localhost", 8088); ) {

        final BufferedWriter writer =
            new BufferedWriter(
                new OutputStreamWriter(new ObjectOutputStream(socket.getOutputStream())));
        write(writer, "服务端");
        socket.shutdownOutput();
        // 输入
        final BufferedReader reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s;
        while ((StrUtil.isNotBlank(s = reader.readLine()))) {
          System.out.print(s);
        }
        writer.close();
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private void write(Writer writer, String str) throws IOException {
      // 输出1
      String strA = StrUtil.format(" {}你好A\r\n", str);
      String strB = StrUtil.format(" {}你好B\r\n", str);
      String strC = StrUtil.format(" {}你好C\r\n", str);
      writer.write(strA);
      writer.write(strB);
      writer.write(strC);
      writer.flush();

      // 输出2
      strA = StrUtil.format(" {}你好D\r\n", str);
      strB = StrUtil.format(" {}你好E\r\n", str);
      strC = StrUtil.format(" {}你好F\r\n", str);
      writer.write(strA);
      writer.write(strB);
      writer.write(strC);
      writer.flush();

      // 输出3
      strA = StrUtil.format(" {}你好G\r\n", str);
      strB = StrUtil.format(" {}你好H\r\n", str);
      strC = StrUtil.format(" {}你好I\r\n", str);
      writer.write(strA);
      writer.write(strB);
      writer.write(strC);
      writer.flush();
    }
  }

  /** 4.1.7 调用Stream的close()方法造成Socket关闭 */
  public static class Case7CallStreamCloseCauseSocketClose {
    @Test
    public void server() throws IOException {
      final byte[] bytes = new byte[10];
      final ServerSocket serverSocket = new ServerSocket(8088);
      final Socket socket = serverSocket.accept();
      final InputStream inputStream = socket.getInputStream();
      int read;
      while ((read = inputStream.read(bytes)) != -1) {
        System.out.println(new String(bytes, 0, read));
      }
      inputStream.close();

      final OutputStream outputStream = socket.getOutputStream();
      socket.close();
      serverSocket.close();
    }

    @Test
    public void client() throws IOException, InterruptedException {
      final Socket socket = new Socket("localhost", 8088);
      final OutputStream outputStream = socket.getOutputStream();
      outputStream.write("我是中国人".getBytes());
      outputStream.close();
      socket.close();
      Thread.sleep(Integer.MAX_VALUE);
    }
  }

  /** 4.1.8 使用 Socket 传递 PNG 图片文件 */
  public static class Case8SenderImg {
    @Test
    public void server() {
      final byte[] bytes = new byte[2048];
      try (ServerSocket serverSocket = new ServerSocket(8088);
          final Socket socket = serverSocket.accept();
          final InputStream inputStream = socket.getInputStream(); ) {
        final File file = new File(FileUtil.getBuildPath("img/aa.jpg"));
        if (file.exists()) {
          file.delete();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
          int readLength;
          while ((readLength = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, readLength);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (FileInputStream fileInputStream =
              new FileInputStream(FileUtil.getBuildPath("img/timg.jpg"));
          final Socket socket = new Socket("localhost", 8088);
          final OutputStream outputStream = socket.getOutputStream()) {

        final byte[] bytes = new byte[2048];
        int readLength;
        while ((readLength = fileInputStream.read(bytes)) != -1) {
          outputStream.write(bytes, 0, readLength);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 4.1.9 TCP 连接的3次“握手”过程 */
  public static class Case9TCP3ShakeHands {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket(8088)) {
        System.out.println("server阻塞开始=" + System.currentTimeMillis());
        serverSocket.accept();
        System.out.println("server阻塞结束=" + System.currentTimeMillis());
        Thread.sleep(Integer.MAX_VALUE);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      System.out.println("client连接准备=" + System.currentTimeMillis());
      try (Socket socket = new Socket("127.0.0.1", 8088)) {
        final OutputStream outputStream = socket.getOutputStream();
        outputStream.write("111".getBytes());
        outputStream.write("11111".getBytes());
        outputStream.write("1111111111".getBytes());
        Thread.sleep(500000000);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("client连接结束=" + System.currentTimeMillis());
    }
  }

  /** 4.1.11 TCP 断开连接的 4次“挥手”过程 */
  public static class Case11Disconnection4Wave {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket(8088)) {
        final Socket socket = serverSocket.accept();
        socket.close();
        serverSocket.close();
        TimeUnit.SECONDS.sleep(2);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try {
        Socket socket = new Socket("localhost", 8088);
        socket.close();
        TimeUnit.SECONDS.sleep(2);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 4.1.12 “握手”的时机与立即传数据的特性<br>
   * 即使没有使用{@link ServerSocket#accept()}，通过抓包工具也会看到进行了3次握手，和4次挥手过程、以及发送消息+确认消息的TCP包，<br>
   * 因此这里得出结论：即使不使用{@link {@link ServerSocket#accept()}}也会连接上
   */
  public static class Case12ShakeHandsOpportunityAndTransferDataImmediately {
    @Test
    public void server() throws IOException, InterruptedException {
      final ServerSocket serverSocket = new ServerSocket(8088);
      Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void client() throws IOException {
      final Socket socket = new Socket("localhost", 8088);
      final OutputStream outputStream = socket.getOutputStream();
      for (int i = 0; i < 3; i++) {
        outputStream.write("1234567890".getBytes());
      }
      outputStream.close();
      socket.close();
    }
  }
  /** 4.1.13 结合多线程 Thread 实现通信 */
  public static class Case13MultiThread {

    public static class BeginRunnable implements Runnable {
      private Socket socket;

      public BeginRunnable(Socket socket) {
        this.socket = socket;
      }

      @Override
      public void run() {
        try (final InputStreamReader reader = new InputStreamReader(socket.getInputStream()); ) {
          char[] charArray = new char[1000];
          int readLength;
          while ((readLength = reader.read(charArray)) != -1) {
            System.out.println(new String(charArray, 0, readLength));
          }
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    @Test
    public void server() {
      final ExecutorService executorService = Executors.newCachedThreadPool();
      try (ServerSocket serverSocket = new ServerSocket(8888)) {
        int runTag = 1;
        while (runTag == 1) {
          final Socket socket = serverSocket.accept();
          executorService.execute(new BeginRunnable(socket));
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() throws InterruptedException {
      final ExecutorService executorService = Executors.newCachedThreadPool();
      for (int i = 0; i < 100; i++) {
        executorService.submit(
            () -> {
              try (Socket socket = new Socket("localhost", 8888)) {
                final OutputStream outputStream = socket.getOutputStream();
                outputStream.write("我是中国人".getBytes());
                socket.shutdownOutput();
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
      }
      executorService.shutdown();
      executorService.awaitTermination(300, TimeUnit.SECONDS);
    }
  }
  /**
   * 4.1.14 服务端与客户端 传对象以及 I/O 流顺序问题<br>
   * 如果server和client 都先 socket.getInputStream()，将会出现阻塞 类似死锁的情况<br>
   * 正确的写法应该是：<br>
   * 1）服务端先获得ObjectlnputStream对象，客户端就要先获得ObjectOutputStream对象<br>
   * 2）服务端先获得ObjectOutputStream对象，客户端就要先获得ObjectlnputStream对象。
   */
  public static class Case14ServerAndClientTransportObjAndIO {
    public static class UserInfo implements Serializable {
      private long id;
      private String username;
      private String password;

      @Override
      public String toString() {
        return "UserInfo{"
            + "id="
            + id
            + ", username='"
            + username
            + '\''
            + ", password='"
            + password
            + '\''
            + '}';
      }

      public UserInfo(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
      }

      public long getId() {
        return id;
      }

      public void setId(long id) {
        this.id = id;
      }

      public String getUsername() {
        return username;
      }

      public void setUsername(String username) {
        this.username = username;
      }

      public String getPassword() {
        return password;
      }

      public void setPassword(String password) {
        this.password = password;
      }
    }

    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket(8888);
          final Socket socket = serverSocket.accept();
          ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
          ObjectOutputStream objectOutputStream =
              new ObjectOutputStream(socket.getOutputStream())) {
        for (int i = 0; i < 5; i++) {
          try {
            final UserInfo userInfo = (UserInfo) objectInputStream.readObject();
            System.out.println("在服务端打印" + userInfo);
            final UserInfo newUserInfo =
                new UserInfo(i + 1, "serverUserName" + (i + 1), "serverPwd" + (i + 1));
            objectOutputStream.writeObject(newUserInfo);
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (Socket socket = new Socket("localhost", 8888);
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
          ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
        for (int i = 0; i < 5; i++) {
          try {
            final UserInfo newUserInfo =
                new UserInfo(i + 1, "clientUserName" + (i + 1), "clientPwd" + (i + 1));
            objectOutputStream.writeObject(newUserInfo);
            final UserInfo userInfo = (UserInfo) objectInputStream.readObject();
            System.out.println("在客户端打印" + userInfo);
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
