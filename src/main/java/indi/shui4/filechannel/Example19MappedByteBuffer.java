package indi.shui4.filechannel;

import com.google.common.base.Stopwatch;
import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class Example19MappedByteBuffer {

  /** map方法的使用 */
  @Test
  public void case1MapUse() {
    try (FileChannel channel = getChannel()) {
      MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 5);
      print(buffer);
      print(buffer);
      print(buffer);
      print(buffer);
      print(buffer);
      System.out.println();
      buffer = channel.map(FileChannel.MapMode.READ_ONLY, 2, 2);
      print(buffer);
      print(buffer);

      Thread.sleep(500);
      System.out.println();
      // 超出映射范围，代码异常
      print(buffer);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void print(MappedByteBuffer buffer) {
    System.out.println((char) buffer.get() + " position" + buffer.position());
  }

  /** 只读模式测试 */
  @Test
  public void case2ReadOnly() {
    try (FileChannel channel = getChannel()) {
      final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 5);
      // 异常，只读模式不能写入
      buffer.putChar('1');
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 可写可读模式（READ_WRITE）的测试 */
  @Test
  public void case3() {
    try (final RandomAccessFile accessFile = getAccessFile();
        final FileChannel channel = accessFile.getChannel()) {
      final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

      print(buffer);
      print(buffer);
      print(buffer);
      print(buffer);
      print(buffer);
      buffer.position(0);
      writeO_S(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeO_S(MappedByteBuffer buffer) {
    buffer.put((byte) 'o');
    buffer.put((byte) 'p');
    buffer.put((byte) 'q');
    buffer.put((byte) 'r');
    buffer.put((byte) 's');
  }

  /**
   * 专用模式（PRIVATE）的测试 <br>
   * 专用模式可以使对文件的更改只针对当前的MappedByteBuffer可视，并不更改底层文件。
   */
  @Test
  public void case4Private() {
    try (FileChannel channel = getChannel()) {
      final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.PRIVATE, 0, 5);
      print(buffer);
      print(buffer);
      print(buffer);
      print(buffer);
      print(buffer);
      buffer.position(0);
      writeO_S(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * force方法的使用 不使用force：165.0 μs <br>
   * 使用force：63.72 ms
   */
  @Test
  public void case5Force() {
    try (FileChannel channel = getChannel()) {
      final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 100);
      final Stopwatch stopwatch = Stopwatch.createStarted();
      for (int i = 0; i < 100; i++) {
        buffer.put("a".getBytes());
        buffer.force();
      }
      stopwatch.stop();
      System.out.println(stopwatch);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * load与isLoaded <br>
   * 在Windows系统中，isLoaded永远为false，建议去Linux系统中测试
   */
  @Test
  public void case6Load_isLoaded() {
    try (FileChannel channel = getChannel()) {
      MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 100);
      System.out.println(buffer + " " + buffer.isLoaded());
      buffer = buffer.load();
      System.out.println(buffer + " " + buffer.isLoaded());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private FileChannel getChannel() throws FileNotFoundException {
    return getAccessFile().getChannel();
  }

  private RandomAccessFile getAccessFile() throws FileNotFoundException {
    return new RandomAccessFile(FileUtil.getBuildPath("filechannel/19/a.txt"), "rw");
  }
}
