package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * 1)getType: {@link ByteBuffer#putChar(char)} ByteBuffer#putXX(xx).. <br>
 * 2)putType: {@link ByteBuffer#getChar(int)} ByteBuffer#getXX(xx)..
 *
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example9PutOrGetType {

  public static void main(String[] args) {
    final ByteBuffer bb = ByteBuffer.allocate(10);
    bb.putLong(Long.MAX_VALUE);
    // position:8
    System.out.println(bb);
    System.out.println(bb.getLong(0));
    bb.position(0);
    System.out.println(bb.getLong());
    // 127,-1,-1,-1,-1,-1,-1,-1,0,0
    System.out.println(BufferUtil.getContent(bb));
  }
}
