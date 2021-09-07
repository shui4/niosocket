package indi.shui4.network.ch1.common.method;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * {@link NetworkInterface#supportsMulticast()}
 *
 * @author shui4
 * @since 1.0
 */
public class Example8SupportsMulticast {

  public static void main(String[] args) throws SocketException {
    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      NetworkInterface eachNetworkInterface = networkInterfaces.nextElement();
      System.out.println("getName获得网络设备名称=" + eachNetworkInterface.getName());
      System.out.println("getDisplayName获得网络设备显示名称=" + eachNetworkInterface.getDisplayName());
      System.out.println("supportsMulticast是否支持多地址广播=" + eachNetworkInterface.supportsMulticast());
      System.out.println();
      System.out.println();
    }
  }
}
