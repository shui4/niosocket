package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * {@link StandardOpenOption#TRUNCATE_EXISTING} <br>
 * 常量枚举Read的使用 现在假设aaa文件中的内容：abcde <br>
 * 结果：什么都没有了
 *
 * @author shui4
 * @since 1.0
 */
public class Example20OpenFile4Truncate_Existing {
  public static void main(String[] args) {
    final File file = new File(FileUtil.getBuildPath("aaa.txt"));
    final Path path = file.toPath();
    try (FileChannel channel = FileChannel.open(path, TRUNCATE_EXISTING, WRITE)) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
