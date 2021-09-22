package indi.shui4.ch6.aio.ch1;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.2 获取通道文件给定区域的锁
 *
 * @author shui4
 * @since 1.0
 */
public class Case2GetChannelFileAreaLock {
  @Test
  public void a() throws ExecutionException, InterruptedException {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      final Future<FileLock> future = channel.lock(0, 3, false);
      final FileLock lock = future.get();
      TimeUnit.SECONDS.sleep(8);
      lock.release();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void b() throws ExecutionException, InterruptedException {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      final Future<FileLock> future = channel.lock(4, 4, false);
      final Stopwatch stopwatch = Stopwatch.createStarted();
      final FileLock lock = future.get();
      System.out.println(stopwatch.stop());
      lock.release();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
