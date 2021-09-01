package indi.shui4.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

/**
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public interface BufferUtil {

  static String getContent(ByteBuffer buffer) {
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
}
