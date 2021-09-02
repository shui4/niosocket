package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;
import org.junit.jupiter.api.Test;

import javax.annotation.PreDestroy;
import java.nio.ByteBuffer;

/**
 * 复制缓冲区 {@link ByteBuffer#duplicate()}
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example17_Duplicate {
  //    byteBuffer1 capacity=5 limit=5 position=2
  //    byteBuffer2 capacity=3 limit=3 position=0
  //    byteBuffer3 capacity=5 limit=5 position=2
  //            3 4 5
  //            1 2 3 4 5
  @Test
  public void case1() {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5});
    byteBuffer1.position(2);
    BufferUtil.print("byteBuffer1", byteBuffer1);
    final ByteBuffer byteBuffer2 = byteBuffer1.slice();
    final ByteBuffer byteBuffer3 = byteBuffer1.duplicate();
    final ByteBuffer byteBuffer4 = byteBuffer1;
    BufferUtil.print("byteBuffer2", byteBuffer2);
    BufferUtil.print("byteBuffer3", byteBuffer3);
    byteBuffer2.position(0);
    for (int i = byteBuffer2.position(); i < byteBuffer2.limit(); i++) {
      System.out.print(byteBuffer2.get(i) + " ");
    }
    System.out.println();
    byteBuffer3.position(0);
    for (int i = byteBuffer3.position(); i < byteBuffer3.limit(); i++) {
      System.out.print(byteBuffer3.get(i) + " ");
    }
  }

  /** 验证复制之后的修改 */
  //  11 22 3 4 5
  //  11 22 3 4 5
  @Test
  public void case2() {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5});
    final ByteBuffer byteBuffer2 = byteBuffer1.duplicate();
    byteBuffer1.put(0, (byte) 11);
    byteBuffer2.put(1, (byte) 22);

    for (int i = byteBuffer1.position(); i < byteBuffer1.limit(); i++) {
      System.out.print(byteBuffer1.get(i) + " ");
    }
    System.out.println();

    for (int i = byteBuffer2.position(); i < byteBuffer2.limit(); i++) {
      System.out.print(byteBuffer2.get(i) + " ");
    }
  }

  public static void main(String[] args) {}
}
