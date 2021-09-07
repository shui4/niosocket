package indi.shui4.network.ch1.common.method;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 获取IP
 *
 * @author shui4
 * @since 2021/9/7(1.0)
 */
public class Example5GetIP {
  /**
   * 获取IP地址的基本信息
   *
   * @throws SocketException
   */
  @Test
  public void case1GetIPBasicInfo() throws SocketException {
    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      NetworkInterface networkInterface = networkInterfaces.nextElement();
      System.out.println("getName获得网络设备名称=" + networkInterface.getName());
      System.out.println("getDisplayName获得网络设备显示名称=" + networkInterface.getDisplayName());
      System.out.println("getInetAddresses获得网络接口的InetAddress信息=");
      final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
      while (inetAddresses.hasMoreElements()) {
        final InetAddress inetAddress = inetAddresses.nextElement();
        System.out.println(
            "      getCanonicalHostName获取此IP地址的完全限定名=" + inetAddress.getCanonicalHostName());
        System.out.println("      getHostName获取此IP地址的主机名=" + inetAddress.getHostName());
        System.out.println("      getHostAddress返回IP地址字符串=" + inetAddress.getHostAddress());
        System.out.print("      getAddress返回此InetAddress对象的原始IP地址");
        final byte[] addressByte = inetAddress.getAddress();
        for (byte b : addressByte) {
          System.out.print(b + " ");
        }
        System.out.println();
        System.out.println();
      }
      System.out.println();
      System.out.println();
    }
  }

  /**
   * 获得本地主机和回环地址的基本信患
   *
   * @throws UnknownHostException
   */
  @Test
  public void case2GetLoopbackAddress() throws UnknownHostException {
    final InetAddress localhost = InetAddress.getLocalHost();
    System.out.print(" localhost.getAddress()地址为=");
    final byte[] localhostAddress = localhost.getAddress();
    for (byte address : localhostAddress) {
      System.out.print(" " + address + " ");
    }
    System.out.println();
    System.out.println(" " + localhost.getClass().getName());
    System.out.println();
    System.out.print(" InetAddress.getLoopbackAddress()地址为=");
    final InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
    for (byte address : loopbackAddress.getAddress()) {
      System.out.print(" " + address + " ");
    }
    System.out.println();
    System.out.println(" " + localhost.getClass().getName());
  }

  /** 根据主机名获取IP地址 */
  @Test
  public void case3GetIpAddrByHostName() throws UnknownHostException {
    final InetAddress springAddress = InetAddress.getByName("spring.io");
    final InetAddress baiduAddress = InetAddress.getByName("www.baidu.com");
    final InetAddress ipStringAddress = InetAddress.getByName("192.168.0.100");
    final InetAddress localhostAddress = InetAddress.getByName("localhost");
    System.out.println(
        localhostAddress.getClass().getName() + " " + localhostAddress.getHostAddress());
    System.out.println(springAddress.getClass().getName() + " " + springAddress.getHostAddress());
    System.out.println(baiduAddress.getClass().getName() + " " + baiduAddress.getHostAddress());
    System.out.println(
        ipStringAddress.getClass().getName() + " " + ipStringAddress.getHostAddress());
  }

  /** 根据主机名获得所有的 IP 地址 */
  @Test
  public void case4GetIpAddrsByHostName() throws UnknownHostException {
    final InetAddress[] springAddrs = InetAddress.getAllByName("spring.io");
    final InetAddress[] baiduAddrs = InetAddress.getAllByName("www.baidu.com");
    final InetAddress[] inStringAddrs = InetAddress.getAllByName("192.168.0.100");

    case4Print(springAddrs, "spring");
    case4Print(baiduAddrs, "baidu");
    case4Print(inStringAddrs, "spring");
  }

  private void case4Print(InetAddress[] addrs, String hostname) {
    System.out.println(hostname);
    for (InetAddress item : addrs) {
      System.out.println(item.getClass().getName() + " " + item.getHostAddress());
    }
    System.out.println();
  }

  /** 根据 IP 地址 byte[]addr 获得 InetAddress 对象 */
  @Test
  public void case5GetInetAddressByByteAddr() throws UnknownHostException {
    byte[] byteArray = {-64, 88, 0, 102};
    final InetAddress myAddress = InetAddress.getByAddress(byteArray);
    System.out.println(myAddress.getHostAddress());
    System.out.println(myAddress.getHostName());
    System.out.println(myAddress.getClass().getName());
  }
  /** 根据主机名和 IP 地址 byte[]addr 获得 InetAddress 对象 */
  @Test
  public void case6GetInetAddressByIpAndAddr() throws UnknownHostException {
    byte[] byteArray = {-64, 88, 0, 102};
    final InetAddress myAddress = InetAddress.getByAddress("zzzzzz", byteArray);
    System.out.println(myAddress.getHostAddress());
    System.out.println(myAddress.getHostName());
    System.out.println(myAddress.getClass().getName());
  }
  /** 获得全限主机名和主机名 */
  @Test
  public void case7GetCanonicalHostNameAndgetHostName() throws UnknownHostException {
    printCase7("A", InetAddress.getLocalHost());
    printCase7("B", InetAddress.getByName("www.ibm.com"));
    printCase7("C", InetAddress.getByName("14.215.177.38"));
  }

  private void printCase7(String id, InetAddress address1) {
    System.out.println(id + "1 " + address1.getCanonicalHostName());
    System.out.println(id + "2 " + address1.getHostName());
    System.out.println();
  }
}
