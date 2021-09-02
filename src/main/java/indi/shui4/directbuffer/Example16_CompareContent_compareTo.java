package indi.shui4.directbuffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * 比较缓冲区内容 {@link ByteBuffer#compareTo(ByteBuffer)}
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example16_CompareContent_compareTo {
  /** 如果在开始与结束的范围之间有一个字节不同，则返回两者的减数。 */
  @Test
  public void case1() {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {3, 4, 5});
    final ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    byteBuffer1.position(0);
    byteBuffer2.position(2);
    // -5
    System.out.println(byteBuffer1.compareTo(byteBuffer2));
  }

  /** 如果在开始与结束的范围之间每个字节都相同，则返回两者remaining）的减数。 */
  @Test
  public void case2() {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {3, 4, 5});
    final ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    byteBuffer1.position(0);
    byteBuffer2.position(2);
    // -5
    System.out.println(byteBuffer1.compareTo(byteBuffer2));
  }
}
