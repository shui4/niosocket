package indi.shui4.directbuffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author xdc
 * @since 2021/8/31(1.0)
 */
public class Example3_Wrap {
  public static void main(String[] args) {

    byte[] bytes = new byte[8];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (i + 1);
    }
    System.out.println(Arrays.toString(bytes));
    final ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
    buffer1.position(2);
    buffer1.limit(6);
    final ByteBuffer buffer2 = ByteBuffer.wrap(bytes, 2, 4);
    System.out.println("buffer1=" + buffer1);
    System.out.println("buffer2=" + buffer2);
    print(buffer1);
    print(buffer2);
  }

  public static void print(ByteBuffer byteBuffer) {
    while (byteBuffer.hasRemaining()) {
      System.out.print(byteBuffer.get());
    }
    System.out.println();
  }
}
