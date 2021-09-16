package indi.shui4.ch6.aio.ch1;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.9 {@link AsynchronousFileChannel#lock(long, long, boolean, Object, CompletionHandler)} 方法的特点
 *
 * @author shui4
 * @since 1.0
 */
public class Case9LockMethodFeature {
  @Test
  public void aProcess() {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      final Stopwatch stopwatch1 = Stopwatch.createStarted();
      channel.lock(
          0,
          3,
          false,
          "我是附加值",
          new CompletionHandler<FileLock, String>() {
            @Override
            public void completed(final FileLock result, final String attachment) {
              try {
                TimeUnit.SECONDS.sleep(9);
                result.release();
                System.out.println(stopwatch1.stop());
              } catch (InterruptedException | IOException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void failed(final Throwable exc, final String attachment) {
              System.out.println("failed");
              System.out.println(exc.getMessage());
            }
          });
      System.out.println("A end time=" + stopwatch.stop());
      Thread.sleep(10_000);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void bProcess() {

    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      final Stopwatch stopwatch1 = Stopwatch.createStarted();
      channel.lock(
          0,
          3,
          false,
          "我是附加值",
          new CompletionHandler<FileLock, String>() {
            @Override
            public void completed(final FileLock result, final String attachment) {
              try {
                TimeUnit.SECONDS.sleep(9);
                result.release();
                System.out.println(stopwatch1.stop());
              } catch (InterruptedException | IOException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void failed(final Throwable exc, final String attachment) {
              System.out.println("failed");
              System.out.println(exc.getMessage());
            }
          });
      System.out.println("B end time=" + stopwatch.stop());
      Thread.sleep(20_000);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
