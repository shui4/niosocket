package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author shui4
 * @since 1.0
 */
public class Example20OpenFile1CreateAndWrite {

  /**
   * 1.枚举常量CREATE和WRITE的使用：发生异常：NoSuchFileException <br>
   *
   * @throws IOException the io exception
   */
  @Test
  public void case1() throws IOException {
    // 这是一个不存在的文件
    final File file = new File(FileUtil.getBuildPath("aaa.txt"));
    final Path path = file.toPath();
    final FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE);
    fileChannel.close();
  }

  /**
   * {@link #case1()}中的接口说明单独使用CREATE常量并不能创建一个文件aaa.txt，这时需要结合WRITE常量 <br>
   * 这里如果文件已经存在，不会更改原有内容
   */
  @Test
  public void case2() throws IOException {
    // 这是一个不存在的文件
    final File file = new File(FileUtil.getBuildPath("aaa.txt"));
    final Path path = file.toPath();
    final FileChannel fileChannel =
        FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    fileChannel.close();
  }
}
