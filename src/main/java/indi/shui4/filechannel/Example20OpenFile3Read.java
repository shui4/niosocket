package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * {@link StandardOpenOption#READ} <br>
 * 常量枚举Read的使用 现在假设aaa文件中的内容：abcde <br>
 * 结果：abcde
 *
 * @author shui4
 * @since 1.0
 */
public class Example20OpenFile3Read {
  public static void main(String[] args) {
    final File file = new File(FileUtil.getBuildPath("aaa.txt"));
    final Path path = file.toPath();
    try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
      final ByteBuffer buffer = ByteBuffer.wrap(new byte[(int) file.length()]);
      channel.read(buffer);
      final byte[] array = buffer.array();
      for (final byte b : array) {
        System.out.print((char) b);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
