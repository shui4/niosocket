package indi.shui4.util;

import cn.hutool.core.collection.CollUtil;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class BufferUtil {

  /** 1~8 */
  public static final byte[] byteArrayIn = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};

  public static String getContent(ByteBuffer buffer) {
    StringBuilder builder = new StringBuilder();
    final int position = buffer.position();
    buffer.position(0);

    if (buffer.hasRemaining()) {
      builder.append(buffer.get());
    }
    while (buffer.hasRemaining()) {
      builder.append(",").append(buffer.get());
    }
    buffer.position(position);
    return builder.toString();
  }

  public static String getContent(FloatBuffer buffer) {
    StringBuilder builder = new StringBuilder();
    final int position = buffer.position();
    buffer.position(0);

    if (buffer.hasRemaining()) {
      builder.append(buffer.get());
    }
    while (buffer.hasRemaining()) {
      builder.append(",").append(buffer.get());
    }
    buffer.position(position);
    return builder.toString();
  }

  public static String getContent(IntBuffer buffer) {
    StringBuilder builder = new StringBuilder();
    final int position = buffer.position();
    buffer.position(0);

    if (buffer.hasRemaining()) {
      builder.append(buffer.get());
    }
    while (buffer.hasRemaining()) {
      builder.append(",").append(buffer.get());
    }
    buffer.position(position);
    return builder.toString();
  }

  public static String getContent(DoubleBuffer buffer) {
    StringBuilder builder = new StringBuilder();
    final int position = buffer.position();
    buffer.position(0);

    if (buffer.hasRemaining()) {
      builder.append(buffer.get());
    }
    while (buffer.hasRemaining()) {
      builder.append(",").append(buffer.get());
    }
    buffer.position(position);
    return builder.toString();
  }

  public static String getContent(CharBuffer buffer) {
    List list = new ArrayList();
    final int position = buffer.position();
    buffer.position(0);

    while (buffer.hasRemaining()) {
      list.add(buffer.get());
    }
    buffer.position(position);
    return CollUtil.join(list, ",");
  }

  public static void print(String id, ByteBuffer byteBuffer) {
    System.out.println(
        id
            + " capacity="
            + byteBuffer.capacity()
            + " limit="
            + byteBuffer.limit()
            + " position="
            + byteBuffer.position());
  }

  public static void print(ByteBuffer byteBuffer) {
    final byte[] array = byteBuffer.array();
    for (byte b : array) {
      if (b == 0) {
        System.out.print("空格 ");
      } else {
        System.out.print((char) b);
      }
    }
  }
}
