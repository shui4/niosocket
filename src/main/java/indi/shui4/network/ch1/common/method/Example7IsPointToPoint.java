package indi.shui4.network.ch1.common.method;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * {@link NetworkInterface#isPointToPoint()}
 *
 * @author shui4
 * @since 1.0
 */
public class Example7IsPointToPoint {

  public static void main(String[] args) {
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface eachNetworkInterface = networkInterfaces.nextElement();
        System.out.println("getName获得网络设备名称=" + eachNetworkInterface.getName());
        System.out.println("getDisplayName获取网络设备显示名称=" + eachNetworkInterface.getDisplayName());
        System.out.println("isPointToPoint是不是点对点设备=" + eachNetworkInterface.isPointToPoint());
        System.out.println();
        System.out.println();
      }

    } catch (SocketException e) {
      e.printStackTrace();
    }
  }
}
