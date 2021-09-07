package indi.shui4.network.ch1.common.method;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * {@link NetworkInterface#getInterfaceAddresses()}
 *
 * @author shui4
 * @since 1.0
 */
public class Example6InterfaceAddress {
  public static void main(String[] args) {
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();
        System.out.println("getName获得网络设备名称=" + networkInterface.getName());
        System.out.println("getDisplayName获得网络设备显示名称=" + networkInterface.getDisplayName());
        List<InterfaceAddress> addressList = networkInterface.getInterfaceAddresses();
        for (InterfaceAddress address : addressList) {
          InetAddress inetAddress = address.getAddress();
          if (inetAddress != null) {
            System.out.println("  address.getAddress()=" + inetAddress);
          }

          inetAddress = address.getBroadcast();
          if (inetAddress != null) {
            System.out.println("  address.getBroadcast()=" + inetAddress);
          }
          System.out.println("  getNetworkPrefixLength=" + address.getNetworkPrefixLength());
          System.out.println();
        }
        System.out.println("====================分割线====================");
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
  }
}
