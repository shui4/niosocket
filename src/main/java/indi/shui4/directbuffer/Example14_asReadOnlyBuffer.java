package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example14_asReadOnlyBuffer {

  public static void main(String[] args) {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(BufferUtil.byteArrayIn);
    final ByteBuffer byteBuffer2 = byteBuffer1.asReadOnlyBuffer();
    System.out.println(byteBuffer1.isReadOnly());
    System.out.println(byteBuffer2.isReadOnly());
    byteBuffer2.put(0, (byte) 99);
  }
}
