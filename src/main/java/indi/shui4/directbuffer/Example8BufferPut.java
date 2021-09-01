package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * {@link ByteBuffer#put(ByteBuffer)}
 *
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example8BufferPut {
  /** if (n > remaining()) throw new BufferOverflowException(); */
  @Test
  public void error() {
    final ByteBuffer bb1 = ByteBuffer.wrap(BufferUtil.byteArrayIn);
    bb1.position(7);

    byte[] byteArrayIn2 = new byte[] {55, 66, 77};
    final ByteBuffer bb2 = ByteBuffer.wrap(byteArrayIn2);

    bb1.put(bb2);

    System.out.println(BufferUtil.getContent(bb1));
  }

  @Test
  public void success() {
    final ByteBuffer bb1 = ByteBuffer.wrap(BufferUtil.byteArrayIn);
    byte[] byteArrayIn2 = new byte[] {55, 66, 77};
    final ByteBuffer bb2 = ByteBuffer.wrap(byteArrayIn2);
    bb1.put(bb2);
    System.out.println(BufferUtil.getContent(bb1));
  }
}
