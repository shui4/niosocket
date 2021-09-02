package indi.shui4.charbuffer;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * charAt
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example3_put_read_subSequence {

  public static void main(String[] args) throws IOException {
    final CharBuffer buffer1 = CharBuffer.allocate(8);
    buffer1.append("ab123456");
    buffer1.position(2);
    // abcde456
    buffer1.put("cde");
    buffer1.rewind();
    for (int i = 0; i < buffer1.limit(); i++) {
      System.out.print(buffer1.get());
    }
    System.out.println();

    buffer1.position(1);
    final CharBuffer buffer2 = CharBuffer.allocate(4);
    System.out.println("A buffer2 position=" + buffer2.position());
    // bcde
    buffer1.read(buffer2); // 相当于 position是1进行导出
    System.out.println("A buffer2 position=" + buffer2.position());
    buffer2.rewind();
    for (int i = 0; i < buffer2.limit(); i++) {
      System.out.print(buffer2.get());
    }
    System.out.println();
    buffer1.position(2);
    // cd
    final CharBuffer buffer3 = buffer1.subSequence(0, 2);
    System.out.println(
        "C buffer3 position="
            + buffer3.position()
            + " capacity="
            + buffer3.capacity()
            + " limit"
            + buffer3.limit());
    for (int i = buffer3.position(); i < buffer3.limit(); i++) {
      System.out.print(buffer3.get());
    }
    System.out.println();
  }
}
