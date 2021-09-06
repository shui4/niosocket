package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example9_Size {
  @Test
  public void case1() {
    final ByteBuffer buffer1 = ByteBuffer.wrap("abcd".getBytes());
    final ByteBuffer buffer2 = ByteBuffer.wrap("cde".getBytes());
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("filechannel/size.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      System.out.println("A position=" + channel.position() + " size=" + channel.size());

      channel.write(buffer1);
      System.out.println("B position=" + channel.position() + " size=" + channel.size());

      channel.position(2);
      System.out.println("C position=" + channel.position() + " size=" + channel.size());

      channel.write(buffer2);

      // 最后D处的position值是5，说明在下一次的write方法进行写入操作中，要在位为5处进行继续写入。
      System.out.println("D position=" + channel.position() + " size=" + channel.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 验证“将该位置设置为大于文件当前大小的值是合法的，
  @Test
  public void case2() {
    try (RandomAccessFile file =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/size.txt"), "rw");
        final FileChannel channel = file.getChannel()) {
      System.out.println("A position=" + channel.position() + " size=" + channel.size());
      System.out.println(channel.read(ByteBuffer.allocate(10), 10000));
      channel.position(10);
      System.out.println("B position=" + channel.position() + " size=" + channel.size());
      // 这里前面没有内容，因此会使用空格表示
      channel.write(ByteBuffer.wrap("z".getBytes()));
      System.out.println("C position=" + channel.position() + " size=" + channel.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
