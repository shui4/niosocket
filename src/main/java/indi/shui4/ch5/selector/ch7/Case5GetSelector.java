package indi.shui4.ch5.selector.ch7;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * 5.7.5 获得 Selector 对象
 *
 * @author shui4
 * @since 1.0
 */
public class Case5GetSelector {

  @Test
  public void case1() {
    try (final Selector selector = Selector.open()) {
      System.out.println(selector);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
