package indi.shui4.filechannel;

import com.google.common.base.Stopwatch;
import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.*;

/**
 * {@link StandardOpenOption#SPARSE} <br>
 *
 * @author shui4
 * @since 1.0
 */
public class Example20OpenFile8Sync {
  /** 不使用Sync的耗时：3.989 ms */
  @Test
  public void case1NoSync() {
    final Path path = new File(FileUtil.getBuildPath("aaa.txt")).toPath();
    try (FileChannel channel = FileChannel.open(path, CREATE_NEW, WRITE)) {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      for (int i = 0; i < 200; i++) {
        channel.write(ByteBuffer.wrap("a".getBytes()));
      }
      System.out.println(stopwatch);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 使用Sync的耗时：137.6 ms */
  @Test
  public void case2Sync() {
    final Path path = new File(FileUtil.getBuildPath("aaa.txt")).toPath();
    try (FileChannel channel = FileChannel.open(path, SYNC, CREATE_NEW, WRITE)) {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      for (int i = 0; i < 200; i++) {
        channel.write(ByteBuffer.wrap("a".getBytes()));
      }
      System.out.println(stopwatch);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * 使用DSYNC的耗时：147.6 ms
   */
  @Test
  public void case3DSync() {
    final Path path = new File(FileUtil.getBuildPath("aaa.txt")).toPath();
    try (FileChannel channel = FileChannel.open(path, DSYNC, CREATE_NEW, WRITE)) {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      for (int i = 0; i < 200; i++) {
        channel.write(ByteBuffer.wrap("a".getBytes()));
      }
      System.out.println(stopwatch);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
