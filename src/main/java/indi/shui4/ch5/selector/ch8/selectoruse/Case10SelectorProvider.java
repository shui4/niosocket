package indi.shui4.ch5.selector.ch8.selectoruse;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

/**
 * 5.8.10 获得 SelectorProvider provider 对象
 *
 * @author shui4
 * @since 1.0
 */
public class Case10SelectorProvider {

  public static void main(String[] args) throws IOException {
    final SelectorProvider provider1 = SelectorProvider.provider();
    final SelectorProvider provider2 = Selector.open().provider();
    System.out.println(provider1);
    System.out.println(provider2);
  }
}
