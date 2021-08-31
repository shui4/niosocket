package indi.shui4.directbuffer;

import java.nio.CharBuffer;

/**
 * @author xdc
 * @since 2021/8/31(1.0)
 */
public class Example1 {
  public static void main(String[] args) {
    CharBuffer charBuffer = CharBuffer.wrap(new char[] {'a', 'b', 'c', 'd', 'e', 'f'});
    while (charBuffer.hasRemaining()) {
      charBuffer.put('q');
    }
    charBuffer.rewind();
    while (charBuffer.hasRemaining()) {
      System.out.println(charBuffer.get());
    }
    System.out.println(charBuffer);
  }
}
