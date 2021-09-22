package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example11_TransferTo {

  // 如果给定的位置大于该文件的当前大小，则不传输任何字节
  @Test
  public void case1() {
    try (FileChannel channel1 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/aa.txt"), "rw").getChannel();
        FileChannel channel2 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/bb.txt"), "rw").getChannel()) {
      channel2.position(8);
      channel1.transferTo(1000, 4, channel2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 正常传输数据的测试
  @Test
  public void case2() {
    try (FileChannel channel1 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/aa.txt"), "rw").getChannel();
        FileChannel channel2 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/bb.txt"), "rw").getChannel()) {
      channel2.position(3);
      // bb文件内容变为：123cde78
      channel1.transferTo(2, 3, channel2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /* 如果count的字节个数大于position到size的字节个数，则传输通道的size- position 个字节数到dest通道的当前位置 */
  @Test
  public void case3() {
    try (FileChannel channel1 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/aa.txt"), "rw").getChannel();
        FileChannel channel2 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/bb.txt"), "rw").getChannel()) {
      // bb文件内容变成和aa文件内容一样
      channel1.transferTo(0, 1000, channel2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 4.验证：如果count的字节个数小于或等于position到size的字节个数，则传输 count个字节数到dest通道的当前位置
  @Test
  public void case4() {
    try (FileChannel channel1 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/aa.txt"), "rw").getChannel();
        FileChannel channel2 =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/bb.txt"), "rw").getChannel()) {

      System.out.println("A position=" + channel1.position());
      // bb：bcdef
      channel1.transferTo(1, 5, channel2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
