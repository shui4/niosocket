package indi.shui4.directbuffer;

import java.nio.ByteBuffer;

/**
 * 扩容
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example18_Expansion {
  /**
   * @param buffer 原
   * @param extendsSize 扩容大小
   * @return 扩容后的buffer
   */
  public static ByteBuffer extendsSize(ByteBuffer buffer, int extendsSize) {
    final ByteBuffer newByteBuffer = ByteBuffer.allocate(buffer.capacity() + extendsSize);
    newByteBuffer.put(buffer);
    return newByteBuffer;
  }

  public static void main(String[] args) {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5});
    final ByteBuffer byteBuffer2 = extendsSize(byteBuffer1, 2);
    final byte[] newArray = byteBuffer2.array();
    for (byte b : newArray) {
      System.out.print(b + " ");
    }
  }
}
