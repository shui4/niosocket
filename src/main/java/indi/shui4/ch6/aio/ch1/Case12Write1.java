package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author shui4
 * @since 1.0
 */
public class Case12Write1 {

  public static void main(String[] args) {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/write.txt"), StandardOpenOption.WRITE)) {

      final ByteBuffer buffer = ByteBuffer.wrap("abcde".getBytes());
      channel.write(buffer, channel.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
