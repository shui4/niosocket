package indi.shui4.network.ch2;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author shui4
 * @since 1.0
 */
public class Example {

  /**
   * 根据索引获得 <br>
   * {@link NetworkInterface#getByIndex(int)}
   */
  @Test
  public void case1GetByIndex() throws SocketException {
    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      NetworkInterface eachNetworkInterface = networkInterfaces.nextElement();
      System.out.println("getName=" + eachNetworkInterface.getName());
      System.out.println("getDisplayName=" + eachNetworkInterface.getDisplayName());
      System.out.println("getIndex=" + eachNetworkInterface.getIndex());
      System.out.println();
    }
    System.out.println();
    System.out.println();
    NetworkInterface networkInterface = NetworkInterface.getByIndex(1);
    System.out.println("---->>" + networkInterface.getName());
  }

  /**
   * 根据网络接口名称获得 <br>
   * {@link NetworkInterface#getByName(java.lang.String)}
   *
   * @throws SocketException the socket exception
   */
  @Test
  public void case2GetByName() throws SocketException {
    NetworkInterface networkInterface = NetworkInterface.getByName("lo");
    System.out.println(networkInterface.getName());
  }

  /**
   * 根据IP获取<br>
   * {@link NetworkInterface#getByInetAddress(java.net.InetAddress)}
   *
   * @throws UnknownHostException the unknown host exception
   * @throws SocketException the socket exception
   */
  @Test
  public void case3ByIp() throws UnknownHostException, SocketException {
    InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
    NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
    System.out.println(networkInterface.getName());
    System.out.println(networkInterface.getDisplayName());
  }
}
