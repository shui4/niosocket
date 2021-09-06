package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 2021/9/3(1.0)
 */
public class Example2_read {

  private static final String PATH = "target\\classes\\b.txt";

  // 验证 int read(ByteBuffer dst ）方法返回值的意义
  @Test
  public void case1() {

    new Example1_writeOpsAndPosition().case1();

    try (FileInputStream fis = new FileInputStream(FileUtil.getBuildPath(PATH));
        final FileChannel channel = fis.getChannel()) {

      final ByteBuffer byteBuffer = ByteBuffer.allocate(5);

      int redLength = channel.read(byteBuffer);
      System.out.println(redLength);

      redLength = channel.read(byteBuffer);
      System.out.println(redLength);

      byteBuffer.clear();
      redLength = channel.read(byteBuffer);
      System.out.println(redLength);
      byteBuffer.clear();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 验证int read（ByteBuffer dst）方法是从通道的当前位置开始读取的
  @Test
  public void case2() {
    try (final FileInputStream fis = new FileInputStream(FileUtil.getBuildPath(PATH));
        final FileChannel channel = fis.getChannel()) {
      channel.position(2);
      final ByteBuffer byteBuffer = ByteBuffer.allocate(5);
      channel.read(byteBuffer);
      final byte[] array = byteBuffer.array();
      for (int i = 0; i < array.length; i++) {
        System.out.println((char) array[i]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 验证int read（ByteBufer dst）方法将字节放入ByteBuffer当前位置
  @Test
  public void case3() {
    try (final FileInputStream fis = new FileInputStream(FileUtil.getBuildPath(PATH));
        final FileChannel channel = fis.getChannel()) {
      channel.position(2);
      final ByteBuffer byteBuffer = ByteBuffer.allocate(5);
      byteBuffer.position(3);
      channel.read(byteBuffer);
      final byte[] array = byteBuffer.array();
      for (byte b : array) {
        if (b == 0) {
          System.out.println("空格");
        } else {
          System.out.println((char) b);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // 验证 read 同步特征
  @Test
  public void case4() throws InterruptedException {

    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath(PATH));
        final FileChannel channel = fileInputStream.getChannel()) {
      for (int i = 0; i < 1; i++) {
        new Thread(getRunnable(channel)).start();
        new Thread(getRunnable(channel)).start();
      }
      TimeUnit.SECONDS.sleep(3);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Runnable getRunnable(FileChannel channel) {
    return () -> {
      final ByteBuffer buffer = ByteBuffer.allocate(5);
      try {
        int readLength = channel.read(buffer);
        while (readLength != -1) {
          final byte[] array = buffer.array();
          System.out.println(new String(array, 0, readLength));
          buffer.clear();
          readLength = channel.read(buffer);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  // 验证read方法从通道读取的数据大于缓冲区容量
  @Test
  public void case5() {
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath(PATH));
        FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer buffer = ByteBuffer.allocate(3);
      System.out.println("A " + channel.position());
      channel.read(buffer);
      System.out.println("B " + channel.position());
      buffer.rewind();
      final byte[] array = buffer.array();
      for (byte b : array) {
        System.out.println((char) b);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // 验证 read 方法从通道读取的字节放入缓冲区的remaining空间中
  @Test
  public void case6() {
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath(PATH));
        final FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer buffer = ByteBuffer.allocate(100);
      buffer.position(1).limit(3);
      channel.read(buffer);
      buffer.rewind();
      for (int i = 0; i < buffer.limit(); i++) {
        final byte b = buffer.get();
        if (b == 0) {
          System.out.println("空格");
        } else {
          System.out.println((char) b);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
