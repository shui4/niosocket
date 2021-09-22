package indi.shui4.filechannel;

import indi.shui4.util.BufferUtil;
import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shui4
 * @since 2021/9/6(1.0)
 */
public class Example8_PositionRead {
  /** 验证 read(ByteBuffer dst, long position) 万法返回值的意义 */
  @Test
  public void case1() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("position/a.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer byteBuffer = ByteBuffer.allocate(2);
      int readLength = channel.read(byteBuffer, 2);
      System.out.println(readLength);
      byteBuffer.clear();
      readLength = channel.read(byteBuffer, 10);
      System.out.println(readLength);

      byteBuffer.clear();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 验证 read(ByteBuffer dst, long position) 方法将字节放入 Byte Buffer 当前位置 */
  @Test
  public void case2() {

    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("position/a.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer buffer = ByteBuffer.allocate(5);
      buffer.position(3);
      channel.read(buffer, 2);
      BufferUtil.print(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 验证 read(ByteBuffer dst, long position) 方法具有同步特性 */
  @Test
  public void case3() {
    // 略
  }

  /** 4.验证 read(ByteBuffer dst, long position) 方法从通道读取的数据大于缓冲区容量 */
  @Test
  public void case4() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("position/a.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer byteBuffer = ByteBuffer.allocate(3);
      channel.read(byteBuffer, 1);
      byteBuffer.rewind();
      for (int i = 0; i < byteBuffer.limit(); i++) {
        System.out.println((char) byteBuffer.get());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 5.验证 read(ByteBuffer dst, long position) 方法从通道读取的字节放入缓冲区的remaining空间中 */
  @Test
  public void case5() {
    try (FileInputStream fileInputStream =
            new FileInputStream(FileUtil.getBuildPath("position/a.txt"));
        final FileChannel channel = fileInputStream.getChannel()) {
      final ByteBuffer byteBuffer = ByteBuffer.allocate(100);
      byteBuffer.position(1).limit(3);
      channel.read(byteBuffer, 2);
      byteBuffer.rewind();
      BufferUtil.print(byteBuffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void caseN() {}
}
