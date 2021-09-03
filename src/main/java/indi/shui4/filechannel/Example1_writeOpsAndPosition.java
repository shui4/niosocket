package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 2021/9/3(1.0)
 */
public class Example1_writeOpsAndPosition {
  // 验证 write(ByteBuffer src)万法是从通道的当前位 开始写入的
  @Test
  public void case1() {
    try (final FileOutputStream fos = new FileOutputStream(FileUtil.getBuildPath("target\\a.txt"));
        final FileChannel fileChannel = fos.getChannel()) {
      final ByteBuffer buffer = ByteBuffer.wrap("abcd".getBytes());
      System.out.println("A fileChannel.position()=" + fileChannel.position());
      System.out.println("write() 1 返回值：" + fileChannel.write(buffer));
      System.out.println("B fileChannel.position()=" + fileChannel.position());
      fileChannel.position(2);
      // 注意：还原buffer的position为0
      buffer.rewind();
      System.out.println("write() 2 返回值：" + fileChannel.write(buffer));
      System.out.println("C fileChannel.position()=" + fileChannel.position());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 验证int write(ByteBuffer src)方法将ByteBuffer的remaining写入通道
  @Test
  public void case2() {
    try (final FileOutputStream fos = new FileOutputStream(FileUtil.getBuildPath("target\\a.txt"));
        final FileChannel fileChannel = fos.getChannel()) {
      final ByteBuffer buffer1 = ByteBuffer.wrap("abcde".getBytes());
      final ByteBuffer buffer2 = ByteBuffer.wrap("12345".getBytes());
      System.out.println("buffer2 remaining：" + buffer2.remaining());
      fileChannel.write(buffer1);
      buffer2.position(1);
      buffer2.limit(3);
      System.out.println("buffer2 remaining：" + buffer2.remaining());
      fileChannel.position(2);
      fileChannel.write(buffer2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // 验证int write(ByteBuffer src)方法具有同步特性
  @Test
  public void case3() {
    try (FileOutputStream fos = new FileOutputStream(FileUtil.getBuildPath("target\\a.txt"));
        final FileChannel fileChannel = fos.getChannel()) {

      for (int i = 0; i < 10; i++) {

        new Thread(
                () -> {
                  try {
                    fileChannel.write(ByteBuffer.wrap("abcde\r\n".getBytes()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                },
                "threadA-" + i)
            .start();

        new Thread(
                () -> {
                  try {
                    fileChannel.write(ByteBuffer.wrap("我是中国人\r\n".getBytes()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                })
            .start();
      }
      TimeUnit.SECONDS.sleep(3);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
