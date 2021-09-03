package indi.shui4.filechannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shui4
 * @since 2021/9/3(1.0)
 */
public class Example1_writeOpsAndPosition {

  public static void main(String[] args) {
    try (final FileOutputStream fos =
            new FileOutputStream(new File("E:\\test\\niosocket\\target\\a.txt"));
        final FileChannel fileChannel = fos.getChannel()) {
      final ByteBuffer buffer = ByteBuffer.wrap("abcd".getBytes());
      System.out.println("A fileChannel.position()=" + fileChannel.position());
      System.out.println("write() 1 返回值：" + fileChannel.write(buffer));
      System.out.println("B fileChannel.position()=" + fileChannel.position());
      fileChannel.position(2);
      // 注意：还原buffer的position为0
      buffer.rewind();
      System.out.println("write() 2 返回值：" + fileChannel.write(buffer));
      System.out.println("C fileChannel.position()=" + fileChannel.position());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
