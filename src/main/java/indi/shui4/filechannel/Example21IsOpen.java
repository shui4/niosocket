package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;

/**
 * {@link AbstractInterruptibleChannel#isOpen()}
 *
 * @author shui4
 * @since 1.0
 */
public class Example21IsOpen {
  public static void main(String[] args) {
    File file = new File(FileUtil.getBuildPath("aa.txt"));
    try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel()) {
      System.out.println(channel.isOpen());
      channel.close();
      System.out.println(channel.isOpen());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
