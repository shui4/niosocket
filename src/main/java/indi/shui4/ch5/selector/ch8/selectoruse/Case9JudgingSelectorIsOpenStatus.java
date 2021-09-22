package indi.shui4.ch5.selector.ch8.selectoruse;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * 5.8.9 判断选择器是否为打开状态
 *
 * @author shui4
 * @since 1.0
 */
public class Case9JudgingSelectorIsOpenStatus {

  public static void main(String[] args) throws IOException {
    final Selector selector = Selector.open();
    System.out.println(selector.isOpen());
    selector.close();
    System.out.println(selector.isOpen());
  }
}
