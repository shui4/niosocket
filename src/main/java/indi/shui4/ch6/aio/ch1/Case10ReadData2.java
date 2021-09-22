package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;

/**
 * @author shui4
 * @since 1.0
 */
public class Case10ReadData2 {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/b.txt"), StandardOpenOption.READ)) {

      final ByteBuffer buffer = ByteBuffer.allocate(3);
      channel.read(
          buffer,
          0,
          "我是附加的参数",
          new CompletionHandler<Integer, String>() {

            @Override
            public void completed(final Integer result, final String attachment) {
              System.out.println("completed attachment=" + attachment);
            }

            @Override
            public void failed(final Throwable exc, final String attachment) {
              System.out.println("failed attachment=" + attachment);
            }
          });
      Thread.sleep(2000);
      final byte[] array = buffer.array();
      for (final byte b : array) {
        System.out.println((char) b);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
