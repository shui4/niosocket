package indi.shui4.charbuffer;

import java.nio.CharBuffer;

/**
 * charAt
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example2_charAt {

  public static void main(String[] args) {
    final CharBuffer charBuffer = CharBuffer.allocate(10);
    charBuffer.append("abcdefg");
    charBuffer.position(2);
    // c
    System.out.println(charBuffer.charAt(0));
    // d
    System.out.println(charBuffer.charAt(1));
    // e
    System.out.println(charBuffer.charAt(2));
  }
}
