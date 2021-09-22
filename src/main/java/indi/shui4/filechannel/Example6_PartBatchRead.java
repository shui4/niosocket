package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example6_PartBatchRead {
  /** 1.验证 read(ByteBuffer[] dsts, int offset, int length) 方法返回值的意义 */
  @Test
  public void case1() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("part\\a.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {

      final ByteBuffer buffer1 = ByteBuffer.allocate(2);
      final ByteBuffer buffer2 = ByteBuffer.allocate(2);
      ByteBuffer[] buffers = {buffer1, buffer2};
      long readLength = channel.read(buffers, 0, 2);
      // 4个字节
      System.out.println(readLength);
      buffer1.clear();
      buffer2.clear();

      readLength = channel.read(buffers, 0, 2);
      System.out.println(readLength);
      buffer1.clear();
      buffer2.clear();

      readLength = channel.read(buffers, 0, 2);
      System.out.println(readLength);
      buffer1.clear();
      buffer2.clear();

      readLength = channel.read(buffers, 0, 2);
      System.out.println(readLength);
      buffer1.clear();
      buffer2.clear();
      //      4
      //      4
      //      4
      //      -1
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 2.验证 read(ByteBuffer[] dsts, int offset, int length) 方法是从通道的当前位置开始读取的 */
  @Test
  public void case2() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("part\\a.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      channel.position(2);

      final ByteBuffer buffer1 = ByteBuffer.allocate(2);
      final ByteBuffer buffer2 = ByteBuffer.allocate(2);
      ByteBuffer[] buffers = {buffer1, buffer2};
      channel.read(buffers, 0, 2);
      for (ByteBuffer buffer : buffers) {
        final byte[] array = buffer.array();
        for (byte b : array) {
          System.out.print((char) b);
        }
        System.out.println();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 3.验证 read(ByteBuffer[] dsts, int offset, int length) 方法将字节放入ByteBuffer当前位置 */
  @Test
  public void case3() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("part\\b.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {

      final ByteBuffer buffer1 = ByteBuffer.allocate(8);
      buffer1.position(3);
      final ByteBuffer buffer2 = ByteBuffer.allocate(8);
      buffer1.position(4);
      ByteBuffer[] byteBuffers = {buffer1, buffer2};

      channel.read(byteBuffers, 0, 2);
      //      空格 空格 空格 空格 2     3  e    1
      //      空格 空格 空格 空格 空格 空格 空格 空格
      for (ByteBuffer byteBuffer : byteBuffers) {
        final byte[] array = byteBuffer.array();
        for (byte b : array) {
          if (b == 0) {
            System.out.print("空格 ");
          } else {
            System.out.print((char) b);
          }
        }
        System.out.println();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 4.验证 read(ByteBuffer[] dsts, int offset, int length) 方法具有同步特性 */
  @Test
  public void case4() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("part\\read6_4.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      for (int i = 0; i < 10; i++) {

        new Thread(
                () -> {
                  final ByteBuffer buffer1 = ByteBuffer.allocate(8);
                  final ByteBuffer buffer2 = ByteBuffer.allocate(8);
                  ByteBuffer[] bufferArray = {buffer1, buffer2};
                  try {
                    long readLength = channel.read(bufferArray, 0, 2);
                    while (readLength != -1) {
                      synchronized (Example6_PartBatchRead.class) {
                        for (ByteBuffer buffer : bufferArray) {
                          final byte[] array = buffer.array();
                          for (byte b : array) {
                            System.out.print((char) b);
                          }
                        }
                      }
                      buffer1.clear();
                      buffer2.clear();
                      readLength = channel.read(bufferArray, 0, 2);
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                })
            .start();

        new Thread(
                () -> {
                  final ByteBuffer buffer1 = ByteBuffer.allocate(8);
                  final ByteBuffer buffer2 = ByteBuffer.allocate(8);
                  ByteBuffer[] bufferArray = {buffer1, buffer2};
                  try {
                    long readLength = channel.read(bufferArray, 0, 2);
                    while (readLength != -1) {
                      synchronized (Example6_PartBatchRead.class) {
                        for (ByteBuffer buffer : bufferArray) {
                          final byte[] array = buffer.array();
                          for (byte b : array) {
                            System.out.print((char) b);
                          }
                        }
                      }
                      buffer1.clear();
                      buffer2.clear();
                      readLength = channel.read(bufferArray, 0, 2);
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                })
            .start();
      }

      TimeUnit.SECONDS.sleep(3);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /** 5.验证 read(ByteBuffer[] dsts, int offset, int length) 方法从通道读取的数据大于缓冲区容量 */
  @Test
  public void case5() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("part\\read6_5.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {

      final ByteBuffer buffer1 = ByteBuffer.allocate(2);
      final ByteBuffer buffer2 = ByteBuffer.allocate(2);
      ByteBuffer[] bufferArray = {buffer1, buffer2};
      System.out.println("A " + channel.position());
      final long readLength = channel.read(bufferArray, 0, 2);
      System.out.println("B " + channel.position() + " readLength=" + readLength);

      buffer1.rewind();
      buffer2.rewind();
      for (ByteBuffer buffer : bufferArray) {
        final byte[] array = buffer.array();
        for (byte b : array) {
          System.out.print((char) b);
        }
        System.out.println();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 6.验证 read(ByteBuffer[] dsts, int offset, int length) 方法从通道读取的字节放入缓冲区的remaining空间中 */
  @Test
  public void case6() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("part\\read6_6.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer buffer1 = ByteBuffer.allocate(7);
      buffer1.position(1).limit(3);

      final ByteBuffer buffer2 = ByteBuffer.allocate(7);
      buffer2.position(2).limit(5);

      ByteBuffer[] bufferArray = {buffer1, buffer2};

      channel.read(bufferArray, 0, 2);
      buffer1.rewind();
      buffer2.rewind();
      for (ByteBuffer buffer : bufferArray) {
        final byte[] array = buffer.array();
        for (byte b : array) {
          if (b == 0) {
            System.out.print(" 空格 ");
          } else {
            System.out.print((char) b);
          }
        }
        System.out.println();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
