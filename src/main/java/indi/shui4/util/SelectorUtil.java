package indi.shui4.util;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * @author shui4
 * @since 1.0
 */
public class SelectorUtil {

  public static void printKeysInfo(Selector selector) {
    final Set<SelectionKey> keys = selector.keys();
    final Set<SelectionKey> selectionKeys = selector.selectedKeys();
    System.out.println("keys.size()=" + keys.size());
    System.out.println("selectionKeys.size()=" + selectionKeys.size());
  }
}
