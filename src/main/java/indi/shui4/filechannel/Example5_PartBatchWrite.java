package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example5_PartBatchWrite {
  /** 1.验证 write(ByteBuffer[] srcs, int offset, int length) 方法是从通道的当前位置开始写入的 */
  @Test
  public void case1() {
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("part/a.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {

      final ByteBuffer buffer1 = ByteBuffer.wrap("abcde".getBytes());
      final ByteBuffer buffer2 = ByteBuffer.wrap("12345".getBytes());
      ByteBuffer[] bufferArray = {buffer1, buffer2};
      channel.write(ByteBuffer.wrap("qqqqq".getBytes()));
      channel.position(2);
      channel.write(bufferArray, 0, 2);
      // qqabcde12345
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 2.验证 write(ByteBuffer[] srcs, int offset, int length) 方法将ByteBuffer的remaining写入通道 */
  @Test
  public void case2() {
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("part/b.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      final ByteBuffer buffer1 = ByteBuffer.wrap("abcde".getBytes());

      final ByteBuffer buffer2 = ByteBuffer.wrap("12345".getBytes());
      buffer2.position(1).limit(3);

      final ByteBuffer buffer3 = ByteBuffer.wrap("d1e1f1".getBytes());
      buffer3.position(2).limit(4);

      ByteBuffer[] bufferArray = {buffer1, buffer2, buffer3};
      // 23e1
      channel.write(bufferArray, 1, 2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /** 验证 write(ByteBuffer[] srcs, int offset, int length)方法具有同步特性 */
  @Test
  public void case3(){
    // 略
  }
}
