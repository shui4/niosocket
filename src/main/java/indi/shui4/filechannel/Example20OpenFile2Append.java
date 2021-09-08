package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * {@link StandardOpenOption#APPEND}
 * 现在假设aaa文件中的内容：abcde <br>
 * 运行下面的代码之后结果变为：abcde123
 *
 * @author shui4
 * @since 1.0
 */
public class Example20OpenFile2Append {
  public static void main(String[] args) {
    final Path path = new File(FileUtil.getBuildPath("aaa.txt")).toPath();
    try (FileChannel channel = FileChannel.open(path, StandardOpenOption.APPEND)) {
      channel.write(ByteBuffer.wrap("123".getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
