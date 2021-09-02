package indi.shui4.directbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 如果字节顺序不一致，那么获取数据时会出现错误
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example14_OrderError {
  public static void main(String[] args) {
    final ByteBuffer byteBuffer1 = ByteBuffer.allocate(8);
    byteBuffer1.order(ByteOrder.BIG_ENDIAN);
    byteBuffer1.putInt(123);
    byteBuffer1.putInt(567);
    byteBuffer1.flip();
    System.out.println(byteBuffer1.getInt());
    System.out.println(byteBuffer1.getInt());
    System.out.println("=====");
    final ByteBuffer byteBuffer2 = ByteBuffer.wrap(byteBuffer1.array());
    byteBuffer2.order(ByteOrder.LITTLE_ENDIAN);
    System.out.println(byteBuffer2.getInt());
    System.out.println(byteBuffer2.getInt());
  }
}
