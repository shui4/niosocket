package indi.shui4.directbuffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example6Error {
  // region 错误
  // buffer溢出 BufferUnderflowException，遍历 inArray时，buffer position超过limit
  @Test
  public void error1() {
    byte[] byteArrayIn1 = new byte[] {1, 2, 3, 4, 5, 6, 7};
    final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    byteBuffer.position(9);
    byteBuffer.put(byteArrayIn1, 0, 4);
  }

  // outArray比 buffer 长度大 BufferUnderflowException
  // byteArrayIn遍历500次，这里buffer到101的时候会超过limit
  @Test
  public void error2() {
    byte[] byteArrayIn = new byte[500];

    for (int i = 0; i < byteArrayIn.length; i++) {
      byteArrayIn[i] = (byte) (i + 1);
    }
    final ByteBuffer bb = ByteBuffer.allocate(100);
    bb.get(byteArrayIn);
  }

  // 数组越界：IndexOutOfBoundsException
  // 数组长度为7，从0 到 10（buffer容量） 的遍历
  @Test
  public void error3() {
    byte[] byteArrayIn = new byte[] {1, 2, 3, 4, 5, 6, 7};
    final ByteBuffer buffer = ByteBuffer.allocate(10);
    buffer.put(byteArrayIn, 0, buffer.capacity());
  }

  // endregion

  // 解决方式
  @Test
  public void test2() {
    byte[] byteArrayIn1 = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    int getArrayIndex = 0;
    while (getArrayIndex < byteArrayIn1.length) {
      final int readLength = Math.min(byteBuffer.remaining(), byteArrayIn1.length - getArrayIndex);
      byteBuffer.put(byteArrayIn1, getArrayIndex, readLength);
      byteBuffer.flip();
      final byte[] getArray = byteBuffer.array();
      for (int i = 0; i < byteBuffer.limit(); i++) {
        System.out.print(getArray[i] + " ");
      }
      getArrayIndex += readLength;
      System.out.println();
      byteBuffer.clear();
    }
  }
}
