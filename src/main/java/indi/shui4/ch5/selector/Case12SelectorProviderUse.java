package indi.shui4.ch5.selector;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.nio.channels.spi.SelectorProvider;

/**
 * 5.12 SelectorProvider 类的使用
 *
 * @author shui4
 * @since 1.0
 */
public class Case12SelectorProviderUse {
  public static void main(String[] args) throws IOException {

    final SelectorProvider provider = SelectorProvider.provider();

    print("provider", provider);
    print("provider.openSelector()", provider.openSelector());
    print("provider.openDatagramChannel()", provider.openDatagramChannel());
    print(
        "provider.openDatagramChannel(StandardProtocolFamily.INET)",
        provider.openDatagramChannel(StandardProtocolFamily.INET));
    print(
        "provider.openDatagramChannel(StandardProtocolFamily.INET6)",
        provider.openDatagramChannel(StandardProtocolFamily.INET6));
    print("provider.openPipe()", provider.openPipe());
    print("provider.openServerSocketChannel()", provider.openServerSocketChannel());
    print("provider.openSocketChannel()", provider.openSocketChannel());
    print("provider.inheritedChannel()", provider.inheritedChannel());
  }

  private static void print(String id, final Object obj) {
    if (obj != null) {
      System.out.println(id + "=" + obj.getClass().getName());
      return;
    }
    System.out.println(id + "=" + null);
  }
}
