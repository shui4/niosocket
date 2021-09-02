package indi.shui4.directbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 获取字节顺序
 *
 * @author shui4
 * @since 2021/9/2(1.0)
 */
public class Example14_Order {
  public static void main(String[] args) {
    int value = 123456789;
    print(value, null);
    print(value, ByteOrder.BIG_ENDIAN);
    print(value, ByteOrder.LITTLE_ENDIAN);
  }

  private static void print(int value, ByteOrder order) {
    ByteBuffer byteBuffer1 = ByteBuffer.allocate(4);
    System.out.print(byteBuffer1.order() + " ");
    if (order != null) {
      byteBuffer1.order(order);
    }
    System.out.print(byteBuffer1.order() + " ");
    byteBuffer1.putInt(value);
    byte[] byteArray = byteBuffer1.array();
    for (byte b : byteArray) {
      System.out.print(b + " ");
    }
    System.out.println();
  }
}
