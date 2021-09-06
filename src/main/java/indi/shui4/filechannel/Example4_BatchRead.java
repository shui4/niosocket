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
public class Example4_BatchRead {

  // 验证 read(ByteBuffer[] dsts)方法返回值的意义

  @Test
  public void case1() {
    /**
     * readLength:4 <br>
     * readLength:3 <br>
     * readLength:-1
     */
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath("c.txt"));
        FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer buffer1 = ByteBuffer.allocate(2);
      final ByteBuffer buffer2 = ByteBuffer.allocate(2);
      ByteBuffer[] bufferArray = {buffer1, buffer2};
      long readLength = channel.read(bufferArray);
      System.out.println("readLength:" + readLength);
      buffer1.clear();
      buffer2.clear();

      readLength = channel.read(bufferArray);
      System.out.println("readLength:" + readLength);
      buffer1.clear();
      buffer2.clear();

      readLength = channel.read(bufferArray);
      System.out.println("readLength:" + readLength);
      buffer1.clear();
      buffer2.clear();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // 验证read(ByteBuffer[] dsts)方法是从通道的当前位置开始读取的
  @Test
  public void case2() {
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath("c.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      channel.position(2);

      final ByteBuffer buffer1 = ByteBuffer.allocate(2);
      final ByteBuffer buffer2 = ByteBuffer.allocate(2);

      final ByteBuffer[] bufferArray = {buffer1, buffer2};
      channel.read(bufferArray);
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

  // 3.验证read(ByteBuffer[] dsts)方法将字节放入ByteBuffer当前位置
  @Test
  public void case3() {
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath("c.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      channel.position(2);

      final ByteBuffer buffer1 = ByteBuffer.allocate(2);
      final ByteBuffer buffer2 = ByteBuffer.allocate(2);
      final ByteBuffer[] bufferArray = {buffer1, buffer2};
      buffer1.position(1);
      channel.read(bufferArray);
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

  // 4.验证read(ByteBuffer[] dsts)方法具有同步特性
  @Test
  public void case4() {
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath("d.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      for (int i = 0; i < 10; i++) {
        new Thread(
                () -> {
                  final ByteBuffer buffer1 = ByteBuffer.allocate(8);
                  final ByteBuffer buffer2 = ByteBuffer.allocate(8);
                  ByteBuffer[] bufferArray = {buffer1, buffer2};
                  try {
                    long readLength = channel.read(bufferArray);
                    while (readLength != -1) {
                      synchronized (Example4_BatchRead.class) {
                        for (ByteBuffer buffer : bufferArray) {
                          final byte[] array = buffer.array();
                          for (byte b : array) {
                            System.out.print((char) b);
                          }
                          System.out.println();
                        }
                      }
                      buffer1.clear();
                      buffer2.clear();
                      readLength = channel.read(bufferArray);
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
                    long readLength = channel.read(bufferArray);
                    while (readLength != -1) {
                      synchronized (Example4_BatchRead.class) {
                        for (ByteBuffer buffer : bufferArray) {
                          final byte[] array = buffer.array();
                          for (byte b : array) {
                            System.out.print((char) b);
                          }
                          System.out.println();
                        }
                      }
                      buffer1.clear();
                      buffer2.clear();
                      readLength = channel.read(bufferArray);
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

  // 5.验证read(ByteBuffer[] dsts)方法从通道读取的数据大于缓冲区容量
  @Test
  public void case5() {
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath("d.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer buffer1 = ByteBuffer.allocate(2);
      final ByteBuffer buffer2 = ByteBuffer.allocate(2);
      ByteBuffer[] bufferArray = {buffer1, buffer2};
      System.out.println("A " + channel.position());
      long readLength = channel.read(bufferArray);
      System.out.println("B " + channel.position());
      buffer1.rewind();
      buffer2.rewind();

      // ByteBuffer[]缓冲区数组总共的remaining剩余容量为多少，就从通道中读多少字节的数据。
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

  // 验证read(ByteBuffer[] dsts)方法从通道读取的字节放入缓冲区的remaining空间中
  @Test
  public void case6() {
    try (FileInputStream fileInputStream = new FileInputStream(FileUtil.getBuildPath("c.txt"));
        FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer buffer1 = ByteBuffer.allocate(7);
      buffer1.position(1).limit(3);
      final ByteBuffer buffer2 = ByteBuffer.allocate(7);
      buffer2.position(2).limit(4);
      ByteBuffer[] bufferArray = {buffer1, buffer2};
      channel.read(bufferArray);
      buffer1.rewind();
      buffer2.rewind();
      // 空格	1	2	空格	空格	空格	空格
      // 空格	空格	3	b	空格	空格	空格
      for (ByteBuffer buffer : bufferArray) {
        final byte[] array = buffer.array();
        for (byte b : array) {
          if (b == 0) {
            System.out.print("空格\t");
          } else {
            System.out.print((char) b + "\t");
          }
        }
        System.out.println();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
