package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.8 执行锁定与传入附件及整合接口 CompletionHandler
 *
 * @author shui4
 * @since 1.0
 */
public class Case8CallLockingAndAttachmentMergeCompletionHandler {

  public static void main(String[] args) {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      System.out.println("A begin time=" + System.currentTimeMillis());
      channel.lock(
          "我是附加值A",
          new CompletionHandler<FileLock, String>() {
            @Override
            public void completed(final FileLock result, final String attachment) {
              try {
                Thread.sleep(9000);
                result.release();
                System.out.println("A release lock time=" + System.currentTimeMillis());
              } catch (InterruptedException | IOException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void failed(final Throwable exc, final String attachment) {
              System.out.println("failed");
              System.out.println("exc.getMessage()=" + exc.getMessage());
            }
          });
      System.out.println("A end time=" + System.currentTimeMillis());
      TimeUnit.SECONDS.sleep(50);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
