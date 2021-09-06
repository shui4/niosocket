package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * {@link FileLock}
 *
 * @author shui4
 * @since 1.0
 */
public class Example17FileLock {

  /** 1.常用API */
  @Test
  public void case1BeInCommonUseAPI() {
    try (final FileChannel channel = getChannel()) {
      System.out.println("channel.hashCode()=" + channel.hashCode());
      FileLock lock = channel.lock(1, 10, true);
      lockPrint(lock);
      // 释放
      lock.release();
      lock = channel.lock(1, 10, false);
      lockPrint(lock);
      lock.close();
      channel.close();
      lockPrint(lock);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private FileChannel getChannel() throws FileNotFoundException {
    return new RandomAccessFile(FileUtil.getBuildPath("filechannel/17/a.txt"), "rw").getChannel();
  }

  private void lockPrint(FileLock lock) {
    System.out.println(
        "A position="
            + lock.position()
            + " size="
            + lock.size()
            + " isValid="
            + lock.isValid()
            + " isShared"
            + lock.isShared()
            + " channel.hashCode()="
            + lock.channel().hashCode()
            + " acquiredBy().hashCode()="
            + lock.acquiredBy().hashCode());
  }

  @Test
  public void case2Overlaps() {
    try (FileChannel channel = getChannel()) {
      final FileLock lock = channel.lock(1, 10, true);
      System.out.println(lock.overlaps(5, 10));
      lock.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
