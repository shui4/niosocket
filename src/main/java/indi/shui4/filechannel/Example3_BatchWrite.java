package indi.shui4.filechannel;

import com.google.errorprone.annotations.Var;
import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example3_BatchWrite {

  // 验证 write(ByteBuffer[] srcs) 方法时从通道的当前位置开始写入的
  @Test
  public void case1() {
    try (FileOutputStream fileOutputStream = new FileOutputStream(FileUtil.getBuildPath("c.txt"));
        FileChannel channel = fileOutputStream.getChannel()) {
      channel.write(ByteBuffer.wrap("123456".getBytes()));
      channel.position(3);

      final ByteBuffer buffer1 = ByteBuffer.wrap("ooooo1".getBytes());
      final ByteBuffer buffer2 = ByteBuffer.wrap("ooooo2".getBytes());
      ByteBuffer[] byteBufferArray = new ByteBuffer[] {buffer1, buffer2};
      channel.write(byteBufferArray);
      // 123ooooo1ooooo2
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 验证 write(ByteBuffer[] srcs)方法将ByteBuffer的remaining写入通道
  @Test
  public void case2() {
    try (FileOutputStream fileOutputStream = new FileOutputStream(FileUtil.getBuildPath("c.txt"));
        FileChannel channel = fileOutputStream.getChannel()) {
      channel.write(ByteBuffer.wrap("123456".getBytes()));
      channel.position(3);
      final ByteBuffer buffer1 = ByteBuffer.wrap("abcde1".getBytes());
      buffer1.position(1).limit(3);

      final ByteBuffer buffer2 = ByteBuffer.wrap("abcde1".getBytes());
      buffer2.position(2).limit(4);
      ByteBuffer[] bufferArray = new ByteBuffer[] {buffer1, buffer2};
      channel.write(bufferArray);
      // 123bccd
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // 验证 write(ByteBuffer [] srcs)方法具有同步特性
  @Test
  public void case3() {
    try (FileOutputStream fileOutputStream = new FileOutputStream(FileUtil.getBuildPath("d.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      for (int i = 0; i < 10; i++) {

        new Thread(
                () -> {
                  final ByteBuffer buffer1 = ByteBuffer.wrap("ooooo1\r\n".getBytes());
                  final ByteBuffer buffer2 = ByteBuffer.wrap("ooooo2\r\n".getBytes());
                  ByteBuffer[] bufferArray = {buffer1, buffer2};
                  try {
                    channel.write(bufferArray);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                })
            .start();

        new Thread(
                () -> {
                  final ByteBuffer buffer1 = ByteBuffer.wrap("zzzzz1\r\n".getBytes());
                  final ByteBuffer buffer2 = ByteBuffer.wrap("zzzzz2\r\n".getBytes());
                  ByteBuffer[] bufferArray = {buffer1, buffer2};
                  try {
                    channel.write(bufferArray);
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
