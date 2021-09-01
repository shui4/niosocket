package indi.shui4.directbuffer;

import cn.hutool.core.lang.Console;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 在使用绝对值操作的时候，position不变
 *
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example7PositionConstant {

  public static void main(String[] args) {
    byte[] byteArrayIn1 = new byte[8];
    for (int i = 0; i < byteArrayIn1.length; i++) {
      byteArrayIn1[i] = (byte) (i + 1);
    }
    Console.log("byteArrayIn1 ->{}", Arrays.toString(byteArrayIn1));
    final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    byteBuffer.put(byteArrayIn1);
    byteBuffer.position(0);
    // 绝对值操作
    byteBuffer.put(2, (byte) 9);
    System.out.println("byteBuffer.get(2):" + byteBuffer.get(2));
    byte[] byteArrayOut = new byte[byteBuffer.capacity()];
    byteBuffer.get(byteArrayOut, 0, byteArrayOut.length);
    for (byte b : byteArrayOut) {
      System.out.print(b + " ");
    }
  }
}
