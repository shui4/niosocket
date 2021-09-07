package indi.shui4.network.ch1.common.method;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取硬件地址 {@link NetworkInterface#getHardwareAddress()}
 *
 * @author shui4
 * @since 2021/9/7(1.0)
 */
public class Example4GetHardwareAddress {
  public static void main(String[] args) throws SocketException {
    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      NetworkInterface networkInterface = networkInterfaces.nextElement();
      System.out.println("getName获得网络设备名称=" + networkInterface.getName());
      System.out.println("getDisplayName获得网络设备显示名称=" + networkInterface.getDisplayName());
      System.out.print("getHardwareAddress获取网卡的物理地址=");
      // 这里获得的是10进制的数据，真正的物理地址是16进制的
      final byte[] byteArray = networkInterface.getHardwareAddress();
      if (byteArray != null && byteArray.length != 0) {
        for (byte b : byteArray) {
          System.out.print(b + " ");
        }
        System.out.println();
      }
      System.out.println();
      System.out.println();
    }
  }
}
