package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.function.BiConsumer;

/**
 * {@link FileChannel#transferFrom(java.nio.channels.ReadableByteChannel, long, long)}
 *
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example12_TransferFrom {

  // 如果给定的位置大于该文件的当前大小，则不传输任何字节
  @Test
  public void case1() {
    run(
        (channelA, channelB) -> {
          try {
            channelB.position(4);
            System.out.println(channelA.transferFrom(channelB, 100, 2));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  private void run(BiConsumer<FileChannel, FileChannel> biConsumer) {
    try (FileChannel channelA =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/12/a.txt"), "rw").getChannel();
        FileChannel channelB =
            new RandomAccessFile(FileUtil.getBuildPath("filechannel/12/b.txt"), "rw")
                .getChannel()) {
      biConsumer.accept(channelA, channelB);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 正常
  @Test
  public void case2() {
    run(
        (a, b) -> {
          try {
            b.position(4);
            // a:abc56fg
            System.out.println(a.transferFrom(b, 3, 2));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  // 3.验证：如果count的字节个数大于src.remaining，则通道的src.remaining字节数传输到当前通道的position位置
  @Test
  public void case3() {
    run(
        (a, b) -> {
          try {
            b.position(2);
            // a:a3456789
            System.out.println(a.transferFrom(b, 1, 200));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
  // 4.验证：如果count的字节个数小于或等于src.remaining，则count个字节传输到当前通道的 position位置
  @Test
  public void case4() {
    run(
        (a, b) -> {
          try {
            b.position(2);
            // a:a34defg
            System.out.println(a.transferFrom(b, 1, 2));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
}
