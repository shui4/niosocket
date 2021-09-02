package indi.shui4.charbuffer;

import java.nio.CharBuffer;

/**
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example5_length {
  public static void main(String[] args) {
    final CharBuffer charBuffer1 = CharBuffer.wrap("abcd");
    print(charBuffer1);
    System.out.println(charBuffer1.get());
    print(charBuffer1);
    System.out.println(charBuffer1.get());
    print(charBuffer1);
    System.out.println(charBuffer1.get());
    print(charBuffer1);
    System.out.println(charBuffer1.get());
  }

  private static void print(CharBuffer charBuffer1) {
    System.out.println(
        "position="
            + charBuffer1.position()
            + " remaining="
            + charBuffer1.remaining()
            + " length="
            + charBuffer1.length());
  }
}
