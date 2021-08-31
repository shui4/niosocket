package indi.shui4.directbuffer;

import java.nio.ByteBuffer;

/**
 * @author xdc
 * @since 2021/8/31(1.0)
 */
public class Example4GetPutOpt {
  public static void main(String[] args) {
    final ByteBuffer buffer = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5});
    while (buffer.hasRemaining()) {
      buffer.put((byte) 1);
    }
    buffer.rewind();
    while (buffer.hasRemaining()) {
      System.out.println(buffer.get());
    }
  }
}
