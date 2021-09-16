package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 6.1.4 返回此通道文件当前大小与通道打开状态
 *
 * @author shui4
 * @since 1.0
 */
public class Case4ChannelFileSizeAndChannelOpenStatus {
  public static void main(String[] args) {
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      System.out.println("File size=" + channel.size());
      System.out.println("A isOpen=" + channel.isOpen());
      channel.close();
      System.out.println("B isOpen=" + channel.isOpen());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
