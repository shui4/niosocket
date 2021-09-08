package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * {@link StandardOpenOption#CREATE_NEW} <br>
 * 这里会发生异常，可以使用 {@link StandardOpenOption#CREATE} 代替 则不会出现异常
 *
 * @author shui4
 * @since 1.0
 */
public class Example20OpenFile5CreateNew {
  public static void main(String[] args) {
    final File file = new File(FileUtil.getBuildPath("bbb.txt"));
    final Path path = file.toPath();
    try (FileChannel channel = FileChannel.open(path, CREATE_NEW, WRITE)) {

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
