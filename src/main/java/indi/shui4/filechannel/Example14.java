package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * {@link FileChannel#tryLock(long, long, boolean)}
 *
 * @author shui4
 * @since 1.0
 */
public class Example14 {
  @Test
  public void test1ExclusiveLock() {
    try (FileChannel fileChannel = getChannel()) {
      System.out.println("A begin");
      final FileLock fileLock = fileChannel.tryLock(0, 5, false);
      System.out.println("A end 获得了锁 fileLock=" + fileLock);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test2SharedLock() {
    try (FileChannel fileChannel = getChannel()) {
      System.out.println("B begin");
      final FileLock fileLock = fileChannel.tryLock(0, 5, true);
      System.out.println("B end 未获得锁 fileLock=" + fileLock);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static FileChannel getChannel() throws FileNotFoundException {
    return new RandomAccessFile(FileUtil.getBuildPath("filechannel/14/a.txt"), "rw").getChannel();
  }
}
