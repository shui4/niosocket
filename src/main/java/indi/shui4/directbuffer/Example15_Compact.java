package indi.shui4.directbuffer;

import java.nio.ByteBuffer;

/**
 * {@link ByteBuffer#compact()}
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example15_Compact {
  public static void main(String[] args) {
    //
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6});
    System.out.println(byteBuffer1);
    System.out.println("1 getValue=" + byteBuffer1.get());
    System.out.println(byteBuffer1);
    System.out.println("2 getValue=" + byteBuffer1.get());
    System.out.println(byteBuffer1);
    byteBuffer1.compact();
    System.out.println("byteBuffer1.compact();");
    System.out.println(byteBuffer1);

    final byte[] getByteArray = byteBuffer1.array();
    for (byte b : getByteArray) {
      System.out.print(b + " ");
    }
  }
}
