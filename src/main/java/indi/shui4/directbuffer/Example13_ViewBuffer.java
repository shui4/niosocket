package indi.shui4.directbuffer;

import org.junit.jupiter.api.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example13_ViewBuffer {

  /** 验证：视图缓冲区不是根据字节进行索引，而是根据其特定于类型的值的大小进行索引。 */
  @Test
  public void case1_typeSizeIndex() {
    final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    printPosition("byteBuffer", byteBuffer);
    byteBuffer.putInt(123);
    printPosition("byteBuffer", byteBuffer);
    byteBuffer.putInt(456);
    printPosition("byteBuffer", byteBuffer);
    System.out.println();
    final IntBuffer intBuffer = IntBuffer.allocate(10);
    printPosition("intBuffer", intBuffer);
    intBuffer.put(456);
    printPosition("intBuffer", intBuffer);
    intBuffer.put(789);
    printPosition("intBuffer", intBuffer);
  }

  /** 验证：视图缓冲区提供了相对批量get和put方法，这些方法可在缓冲区和数组或相同类型的其他缓冲区之间传输值的连续序列。 */
  @Test
  public void case2_continuous() {
    final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    byteBuffer.putInt(123);
    byteBuffer.putInt(456);
    byteBuffer.flip();
    System.out.println(
        "byteBuffer position=" + byteBuffer.position() + ",value=" + byteBuffer.getInt());
    System.out.println(
        "byteBuffer position=" + byteBuffer.position() + ",value=" + byteBuffer.getInt());
    System.out.println();
    final IntBuffer intBuffer = IntBuffer.allocate(10);
    intBuffer.put(456);
    intBuffer.put(789);
    intBuffer.flip();
    System.out.println("intBuffer position=" + intBuffer.position() + ",value=" + intBuffer.get());
    System.out.println("byteBuffer position=" + intBuffer.position() + ",value=" + intBuffer.get());
  }

  /** 验证：视图缓冲区可能更高效，这是因为当且仅当其支持的字节缓冲区为直接缓冲区时，它才是直接缓冲区。 */
  @Test
  public void case3_moreEfficient() {
    final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
    byteBuffer.putInt(123);
    byteBuffer.putInt(456);
    byteBuffer.flip();

    System.out.println();

    final IntBuffer intBuffer = byteBuffer.asIntBuffer();
    System.out.println(intBuffer.get());
    System.out.println(intBuffer.get());
    System.out.println();
    System.out.println("bytebuffer是直接缓冲区，效率比较快：");
    System.out.println(byteBuffer);
    System.out.println("由于bytebuffer是直接的，所以intBuffer也是直接缓冲区了：");
    System.out.println(intBuffer);
  }

  private void printPosition(String id, Buffer buffer) {
    System.out.println(id + ":" + buffer.position());
  }
}
