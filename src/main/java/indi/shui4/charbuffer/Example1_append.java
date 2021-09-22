package indi.shui4.charbuffer;

import java.nio.CharBuffer;

/**
 * 重载append
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example1_append {

  public static void main(String[] args) {
    final CharBuffer charBuffer = CharBuffer.allocate(15);
    System.out.println("A " + charBuffer.position());
    charBuffer.append('a');
    System.out.println("B " + charBuffer.position());
    charBuffer.append("bcdefg");
    System.out.println("C " + charBuffer.position());
    charBuffer.append("abcdhijklmn", 3, 8);
    final char[] newArray = charBuffer.array();
    for (char c : newArray) {
      System.out.print(c);
    }
    System.out.println();
    System.out.println("charbuffer capacity=" + charBuffer.capacity());
    System.out.println(charBuffer.position());
  }
}
