package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author shui4
 * @since 1.0
 */
public class Case10ReadData1 {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/b.txt"), StandardOpenOption.READ)) {
      final ByteBuffer buffer = ByteBuffer.allocate(3);
      final Future<Integer> readLength = channel.read(buffer, 0);
      System.out.println("length=" + readLength.get());
      final byte[] array = buffer.array();
      for (final byte b : array) {
        System.out.println((char) b);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
