package indi.shui4.network.ch1.common.method;

import org.junit.jupiter.api.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 子接口处理
 *
 * @author shui4
 * @since 2021/9/7(1.0)
 */
public class Example3SubInterfaceProcessing {
  @Test
  public void demo1() throws SocketException {
    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      final NetworkInterface eachNetworkInterface = networkInterfaces.nextElement();
      System.out.println("eachNetworkInterface 父接口的hashcode=" + eachNetworkInterface.hashCode());
      System.out.println("getName获得网络设备名称=" + eachNetworkInterface.getName());
      System.out.println("getDisplayName获得网络设备显示名称=" + eachNetworkInterface.getDisplayName());
      System.out.println("isVirtual与否为虚拟接口=" + eachNetworkInterface.getDisplayName());
      System.out.println("getParent获得父接口=" + eachNetworkInterface.getParent());
      System.out.println("getSubInterfaces获取子接口信息=");

      // 这里需要在Linux运行才能看到，Windows看不到
      final Enumeration<NetworkInterface> subInterfaces = eachNetworkInterface.getSubInterfaces();
      while (subInterfaces.hasMoreElements()) {
        final NetworkInterface networkInterface = subInterfaces.nextElement();
        System.out.println("    getName获得网络设备名称=" + networkInterface.getName());
        System.out.println("    getDisplayName获得网络设备显示名称=" + networkInterface.getDisplayName());
        System.out.println("    isVirtual与否为虚拟接口=" + networkInterface.getDisplayName());
        System.out.println("    getParent获得父接口=" + networkInterface.getParent());
      }
      System.out.println();
      System.out.println();
    }
  }
}
