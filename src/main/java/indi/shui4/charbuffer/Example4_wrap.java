package indi.shui4.charbuffer;

import java.nio.CharBuffer;

/**
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example4_wrap {
  public static void main(String[] args) {
    final CharBuffer charBuffer1 = CharBuffer.wrap("abcdefg", 3, 5);
    System.out.println(
        "capacity="
            + charBuffer1.capacity()
            + " limit="
            + charBuffer1.limit()
            + " position="
            + charBuffer1.position());
    for (int i = 0; i < charBuffer1.limit(); i++) {
      System.out.println(charBuffer1.get(i));
    }
    System.out.println("isReadOnly():" + charBuffer1.isReadOnly());
    charBuffer1.append("我是只读的，不能添加数据，会异常！");
  }
}
