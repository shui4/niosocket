package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.*;

/**
 * {@link StandardOpenOption#DELETE_ON_CLOSE} <br>
 * 这里10s后程序结束时自动删除 aaa.txt文件
 *
 * @author shui4
 * @since 1.0
 */
public class Example20OpenFile6DeleteOnClose {
  public static void main(String[] args) {
    final File file = new File(FileUtil.getBuildPath("aaa.txt"));
    final Path path = file.toPath();
    try (FileChannel channel = FileChannel.open(path, DELETE_ON_CLOSE, CREATE, WRITE)) {
      try {
        TimeUnit.SECONDS.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
