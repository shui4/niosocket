package indi.shui4.filechannel;

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
public class Example20OpenFile7Sparse {
  /** 稀疏文件介绍示例，创建一个非常大的文件（占用2GB左右） */
  @Test
  public void case1CreateBigFile() {
    final Path path = new File(FileUtil.getBuildPath("big.txt")).toPath();
    try (FileChannel channel = FileChannel.open(path, CREATE, WRITE)) {
      int fileSize = Integer.MAX_VALUE;
      fileSize = fileSize + fileSize + fileSize;
      fileSize = fileSize + fileSize + fileSize;
      channel.position(fileSize);
      channel.write(ByteBuffer.wrap("a".getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 上面写入一个a要占用2GB左右，这样就浪了磁盘资源。解决问题的思路是对那些不存储的空间不让其占用硬盘容量，这样就达到了提高硬盘空间利用率的目的，这个需求可以通过创建1个稀疏文件进行实现
   * <br>
   * 这里文件只占用16KB
   */
  @Test
  public void case2() {
    final Path path = new File(FileUtil.getBuildPath("noBig.txt")).toPath();
    try (FileChannel channel = FileChannel.open(path, SPARSE, CREATE_NEW, WRITE)) {
      int fileSize = Integer.MAX_VALUE;
      fileSize = fileSize + fileSize + fileSize;
      channel.position(fileSize);
      channel.write(ByteBuffer.wrap("a".getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
