package indi.shui4.network.ch1.common.method;

import org.junit.jupiter.api.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * {@link NetworkInterface#getMTU()}
 *
 * @author shui4
 * @since 2021/9/7(1.0)
 */
public class Example2GetMTU {

  @Test
  public void demo() throws SocketException {
    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      final NetworkInterface networkInterface = networkInterfaces.nextElement();
      System.out.println("getName获得网络设备名称=" + networkInterface.getName());
      System.out.println("getDisplayName获取网络设备显示名称=" + networkInterface.getDisplayName());
      // -1:网络接口不可用的时候
      System.out.println("getMTU获取最大传输单数=" + networkInterface.getMTU());
      System.out.println();
      System.out.println();
    }
  }
}
