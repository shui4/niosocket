package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class Case12Write2 {

  public static void main(String[] args) {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/write.txt"), StandardOpenOption.WRITE)) {

      final ByteBuffer buffer = ByteBuffer.wrap("abcde".getBytes());
      channel.write(
          buffer,
          channel.size(),
          "我是附加的数据",
          new CompletionHandler<Integer, String>() {
            @Override
            public void completed(final Integer result, final String attachment) {
              System.out.println("completed result=" + result + " attachment=" + attachment);
            }

            @Override
            public void failed(final Throwable exc, final String attachment) {
              System.out.println("failed=" + exc.getMessage());
            }
          });

      TimeUnit.SECONDS.sleep(2);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
