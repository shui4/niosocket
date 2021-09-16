package indi.shui4.ch6.aio.ch1;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.3 实现重量锁定
 *
 * @author shui4
 * @since 1.0
 */
public class Case3OverlapLock {
  @Test
  public void a() throws ExecutionException, InterruptedException {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      final FileLock lock = channel.lock(0, 3, false).get();
      TimeUnit.SECONDS.sleep(8);

      lock.release();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void b() throws InterruptedException, ExecutionException {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      final FileLock lock = channel.lock(2, 1, false).get();
      System.out.println(stopwatch.stop());
      lock.release();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
