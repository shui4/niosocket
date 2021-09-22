package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example10 {

  // 文件内容多余要截取的长度
  @Test
  public void case1() {
    final ByteBuffer buffer1 = ByteBuffer.wrap("12345678".getBytes());
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("filechannel/newtext.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      channel.write(buffer1);
      System.out.println("A size=" + channel.size() + " position=" + channel.position());
      channel.truncate(3);
      System.out.println("B size=" + channel.size() + " position=" + channel.position());
      // 文件内容：123
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 文件内容小于要截取的长度
  @Test
  public void case2() {
    final ByteBuffer buffer1 = ByteBuffer.wrap("12345678".getBytes());
    try (FileOutputStream fileOutputStream =
            new FileOutputStream(FileUtil.getBuildPath("filechannel/abc.txt"));
        final FileChannel channel = fileOutputStream.getChannel()) {
      channel.write(buffer1);
      System.out.println("A size=" + channel.size() + " position=" + channel.position());
      channel.truncate(30_000);
      System.out.println("B size=" + channel.size() + " position=" + channel.position());
      // 文件内容：12345678
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
