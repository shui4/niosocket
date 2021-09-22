package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * {@link FileChannel#write(ByteBuffer, long)}
 *
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example7_PositionWrite {
  /** 1.验证 write(ByteBuffer src, long position) 方法是从通道的指定位置开始写入的 */
  @Test
  public void case1() {
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("position/a.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      final ByteBuffer buffer = ByteBuffer.wrap("abcde".getBytes());
      channel.write(buffer);
      buffer.rewind();
      // ababcde
      channel.write(buffer, 2);
      System.out.println("C channel.position()=" + channel.position());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 2.验证 write(ByteBuffer src, long position) 方法将ByteBuffer的remaining 写入通道 */
  @Test
  public void case2() {
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("position\\a.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      final ByteBuffer buffer1 = ByteBuffer.wrap("abcde".getBytes());
      final ByteBuffer buffer2 = ByteBuffer.wrap("12345".getBytes());
      channel.write(buffer1);
      buffer2.position(1).limit(3);
      // ab23e
      channel.write(buffer2, 2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 3.验证 write(ByteBuffer src, long position) 方法具有同步特性 */
  @Test
  public void case3() {
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("position\\a.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      new Thread(
              () -> {
                System.out.println("线程1运行");
                final ByteBuffer buffer = ByteBuffer.wrap("12345".getBytes());
                try {
                  channel.write(buffer, 0);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();
      new Thread(
              () -> {
                System.out.println("线程2运行");
                final ByteBuffer buffer = ByteBuffer.wrap("67890".getBytes());
                try {
                  channel.write(buffer, 0);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();
      TimeUnit.SECONDS.sleep(3);
      // 上述程序运行结果就是哪个线程在最后运行write方法，文本文件里面就是哪个线程写人的数据。
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /** 4.验证 write(ByteBuffer src, long position) 方法中的 position不变性 */
  @Test
  public void case4() {
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("position\\a.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      System.out.println("A position" + channel.position());
      channel.position(3);
      System.out.println("B position" + channel.position());
      channel.write(ByteBuffer.wrap("abcde".getBytes()), 0);
      System.out.println("C position" + channel.position());
      // 0
      // 3
      // 3
      // position 不改变
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
