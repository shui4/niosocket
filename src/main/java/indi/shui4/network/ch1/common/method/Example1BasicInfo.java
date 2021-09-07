package indi.shui4.network.ch1.common.method;

import org.junit.jupiter.api.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取网络接口的基本信息
 *
 * @author shui4
 * @since 2021/9/7(1.0)
 */
public class Example1BasicInfo {

  @Test
  public void case1() throws SocketException {
    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      final NetworkInterface networkInterface = networkInterfaces.nextElement();
      System.out.println("getName获得网络设备名称=" + networkInterface.getName());
      System.out.println("getDisplayName获取网络设备显示名称=" + networkInterface.getDisplayName());
      System.out.println("getIndex获得网络接口的索引=" + networkInterface.getIndex());
      System.out.println("isUp是否已经开起并运行=" + networkInterface.isUp());
      System.out.println("isLoopback是否为回调接口=" + networkInterface.isLoopback());
      System.out.println();
      System.out.println();
    }
  }
}
