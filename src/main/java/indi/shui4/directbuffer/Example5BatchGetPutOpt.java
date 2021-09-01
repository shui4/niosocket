package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 批量操作
 *
 * @author shui4
 * @since 2021/8/31(1.0)
 */
public class Example5BatchGetPutOpt {
  public static void main(String[] args) {
    byte[] byteArrayIn1 = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
    byte[] byteArrayIn2 = new byte[] {55, 66, 77, 88};
    final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    System.out.println(byteBuffer.remaining());
    byteBuffer.put(byteArrayIn1);
    System.out.println(BufferUtil.getContent(byteBuffer));
    byteBuffer.position(2);
    byteBuffer.put(byteArrayIn2, 1, 3);
    System.out.println(BufferUtil.getContent(byteBuffer));
    byteBuffer.position(1);
    final byte[] byteArrayOut = new byte[byteBuffer.capacity()];
    byteBuffer.get(byteArrayOut, 3, 4);
    System.out.println(Arrays.toString(byteArrayOut));
    System.out.println(byteBuffer.remaining());
    System.out.println(byteBuffer.get());
  }
}
