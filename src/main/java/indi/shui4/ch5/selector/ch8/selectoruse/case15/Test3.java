package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import indi.shui4.util.SelectorUtil;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * 3. 在新创建的选择器中，3个集合都是空集合
 *
 * @author shui4
 * @since 1.0
 */
public class Test3 {
  public static void main(String[] args) {
    try (Selector selector = Selector.open()) {
      SelectorUtil.printKeysInfo(selector);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
