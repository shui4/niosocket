package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 5. 多线程环境下删除键集中的键会导致 ConcurrentModificationException 异常
 *
 * @author shui4
 * @since 1.0
 */
public class Test5 {
  public static void main(String[] args) throws InterruptedException {
    Set set = new HashSet();
    set.add("abc1");
    set.add("abc2");
    set.add("abc3");
    set.add("abc4");
    set.add("abc5");
    set.add("abc6");
    new Thread(
            () -> {
              try {
                Thread.sleep(1_500);
                set.remove("abc3");
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();

    final Iterator iterator = set.iterator();
    while (iterator.hasNext()) {
      Thread.sleep(1_000);
      Object next = iterator.next();
    }
  }
}
