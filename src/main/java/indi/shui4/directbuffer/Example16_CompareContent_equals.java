package indi.shui4.directbuffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * 比较缓冲区内容
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example16_CompareContent_equals {
  /** 判断是不是自身 */
  @Test
  public void case1() {
    final ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    System.out.println(byteBuffer.equals(byteBuffer));
  }

  /** 判断是不是ByteBuffer实例 */
  @Test
  public void case2() {
    final ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    System.out.println(byteBuffer.equals(1));
  }

  /** 判断remaining指是否一样 */
  @Test
  public void case3() {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {3, 4, 5});
    final ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
    byteBuffer1.position(0);
    byteBuffer2.position(3);
    System.out.println(byteBuffer1.equals(byteBuffer2));
    System.out.println("byteBuffer1:" + byteBuffer1.remaining());
    System.out.println("byteBuffer2:" + byteBuffer2.remaining());
  }

  /** 验证：判断两个缓冲区中的position与limit之间的数据是否完全一样，只要有一个字节不同，就返回false，否则返回true。 */
  @Test
  public void case4() {
    final ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[] {3, 4, 5});
    final ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
    byteBuffer1.position(0);
    byteBuffer1.limit(3);

    byteBuffer2.position(2);
    byteBuffer2.limit(5);
    System.out.println("byteBuffer1.equals(byteBuffer2):" + byteBuffer1.equals(byteBuffer2));
    byteBuffer2.put(3, (byte) 44);
    System.out.println("byteBuffer2 index：3改为44之后");
    System.out.println("byteBuffer1.equals(byteBuffer2):" + byteBuffer1.equals(byteBuffer2));
  }


}
