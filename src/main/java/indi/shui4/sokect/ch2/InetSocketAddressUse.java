package indi.shui4.sokect.ch2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

/**
 * 4.2.8 {@link InetSocketAddress} 类的使用
 *
 * @author shui4
 * @since 1.0
 */
public class InetSocketAddressUse {

  /** 1. 构造方法 {@link InetSocketAddress#InetSocketAddress(InetAddress, int)} 的使用 */
  static class ConstructionMethod {
    @Test
    public void server() {
      try (ServerSocket serverSocket = new ServerSocket()) {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, 8888);
        serverSocket.bind(inetSocketAddress);
        Socket socket = serverSocket.accept();
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void client() {
      try (Socket socket = new Socket("localhost", 8888)) {
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 2. getHostName （）和 getHostString （）方法的区别 */
  static class GetHostNameAndGetHostStringDiff {
    @Test
    public void case1() {
      // 注意，此例需要创建两个InetSocketAddress类的对象才能分析出这两种方法的区别。
      InetSocketAddress inetSocketAddress1 = new InetSocketAddress("192.168.43.123", 80);
      InetSocketAddress inetSocketAddress2 = new InetSocketAddress("192.168.43.123", 80);
      System.out.println(inetSocketAddress1.getHostName());
      System.out.println(inetSocketAddress2.getHostString());
    }

    @Test
    public void case2() {
      InetSocketAddress inetSocketAddress1 = new InetSocketAddress("192.168.43.123", 80);
      System.out.println(inetSocketAddress1.getHostName());
      System.out.println(inetSocketAddress1.getHostString());
    }
  }

  /** 3.获取IP地址InetAddress对象 */
  static class GetInetAddress {
    @Test
    public void main() {
      InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
      InetAddress inetAddress = inetSocketAddress.getAddress();
      byte[] address = inetAddress.getAddress();
      for (byte b : address) {
        System.out.print(b + " ");
      }
    }
  }
  /** 4. 创建未解析的套撞字地址 */
  static class CrateUnresolvedSocketAddress {
    @Test
    public void main() {
      // 输出false的原因是可以对www.baidu.com进行解析
      InetSocketAddress inetSocketAddress1 = new InetSocketAddress("wwww.baidu.com", 80);
      System.out.println(inetSocketAddress1.isUnresolved());

      // 输出true的原本是不能对这个域名解析
      InetSocketAddress inetSocketAddress2 =
          new InetSocketAddress("wwww.baidu.asdfasdfasfdsdacom", 80);
      System.out.println(inetSocketAddress2.isUnresolved());

      // 输出true是因为即使对www.baidu.com进行解析，内部也不解析
      InetSocketAddress inetSocketAddress3 =
          InetSocketAddress.createUnresolved("www.baidu.com", 80);
      System.out.println(inetSocketAddress3.isUnresolved());
      // 输出true的原因是内部从来不解析
      InetSocketAddress inetSocketAddress4 =
          InetSocketAddress.createUnresolved("wwww.baidu.asdfasdfasfdsdacom", 80);
      System.out.println(inetSocketAddress4.isUnresolved());
    }
  }
  
}
