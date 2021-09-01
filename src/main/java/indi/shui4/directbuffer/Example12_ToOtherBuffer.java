package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

/**
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example12_ToOtherBuffer {
  public static void main(String[] args) {
    final ByteBuffer byteBuffer1 = ByteBuffer.allocate(32);
    byteBuffer1.putDouble(1.1D);
    byteBuffer1.putDouble(1.2D);
    byteBuffer1.putDouble(1.3D);
    byteBuffer1.putDouble(1.4D);
    byteBuffer1.flip();
    final DoubleBuffer doubleBuffer = byteBuffer1.asDoubleBuffer();
    System.out.println("doubleBuffer ->" + BufferUtil.getContent(doubleBuffer));
    System.out.println();
    final ByteBuffer byteBuffer2 = ByteBuffer.allocate(16);
    byteBuffer2.putFloat(2.1F);
    byteBuffer2.putFloat(2.2F);
    byteBuffer2.putFloat(2.3F);
    byteBuffer2.putFloat(2.4F);
    byteBuffer2.flip();
    System.out.println("floatBuffer ->" + BufferUtil.getContent(byteBuffer2.asFloatBuffer()));
    System.out.println();
    final ByteBuffer byteBuffer3 = ByteBuffer.allocate(16);
    byteBuffer3.putInt(31);
    byteBuffer3.putInt(32);
    byteBuffer3.putInt(33);
    byteBuffer3.putInt(34);
    byteBuffer3.flip();
    System.out.println("intBuffer ->" + BufferUtil.getContent(byteBuffer3.asIntBuffer()));
    // ...more long、short
  }
}
