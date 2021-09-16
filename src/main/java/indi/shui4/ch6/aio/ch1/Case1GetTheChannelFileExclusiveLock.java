package indi.shui4.ch6.aio.ch1;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.1 获取此通道文件的独占锁
 *
 * @author shui4
 * @since 1.0
 */
public class Case1GetTheChannelFileExclusiveLock {
  @Test
  public void a() throws ExecutionException, InterruptedException {
    final Path path = Paths.get("aio/a.txt");
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
      final Future<FileLock> future = channel.lock();
      final FileLock lock = future.get();
      System.out.println("A   get lock time=" + System.currentTimeMillis());
      final Stopwatch stopwatch = Stopwatch.createStarted();
      TimeUnit.SECONDS.sleep(8);
      lock.release();
      System.out.println("A release lock time=" + System.currentTimeMillis());
      System.out.println(stopwatch.stop());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void b() {
    final Path path = Paths.get("aio/a.txt");
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
      final Future<FileLock> future = channel.lock();
      final Stopwatch stopwatch = Stopwatch.createStarted();
      final FileLock lock = future.get();
      System.out.println(stopwatch.stop());
      System.out.println("lock begin" + System.currentTimeMillis());
      lock.release();
      System.out.println("B get lock time" + System.currentTimeMillis());

    } catch (IOException | InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
