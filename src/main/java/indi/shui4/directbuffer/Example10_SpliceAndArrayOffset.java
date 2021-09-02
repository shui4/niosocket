package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example10_SpliceAndArrayOffset {

  /**
   * 打印中.... ====bb1 java.nio.HeapByteBuffer[pos=5 lim=8 cap=8] 1,2,3,4,5,6,7,8
   *
   * <p>====bb2 6,7,8 java.nio.HeapByteBuffer[pos=0 lim=3 cap=3]
   *
   * <p>打印中.... ====bb1 java.nio.HeapByteBuffer[pos=5 lim=8 cap=8] 1,2,3,4,5,20,21,22
   *
   * <p>====bb2 20,21,22 java.nio.HeapByteBuffer[pos=0 lim=3 cap=3]
   *
   * <p>arrayOffset:0 arrayOffset:5
   */
  @Test
  public void splice() {
    final ByteBuffer bb1 = ByteBuffer.wrap(BufferUtil.byteArrayIn);
    bb1.position(5);
    final ByteBuffer bb2 = bb1.slice();
    print(bb1, bb2);
    bb2.put(0, (byte) 20);
    bb2.put(1, (byte) 21);

    bb1.put(7, (byte) 22);
    print(bb1, bb2);
    System.out.println();
    System.out.println("arrayOffset:" + bb1.arrayOffset());
    // 5，说明bb2的第一个元素相当于bb1的索引为5的偏移1
    System.out.println("arrayOffset:" + bb2.arrayOffset());
  }

  private void print(ByteBuffer bb1, ByteBuffer bb2) {
    System.out.println();
    System.out.println("打印中....");
    System.out.println("====bb1");
    System.out.println(bb1);
    System.out.println(BufferUtil.getContent(bb1));
    System.out.println();
    System.out.println("====bb2");
    System.out.println(BufferUtil.getContent(bb2));
    System.out.println(bb2);
  }
}
