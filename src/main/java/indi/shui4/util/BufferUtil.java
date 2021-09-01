package indi.shui4.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class BufferUtil {

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

  public static final byte[] byteArrayIn = new byte[8];

  static {
    for (int i = 0; i < byteArrayIn.length; i++) {
      byteArrayIn[i] = (byte) (i + 1);
    }
  }
}
