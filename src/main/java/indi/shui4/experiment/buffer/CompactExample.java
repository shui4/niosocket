package indi.shui4.experiment.buffer;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author shui4
 * @since 1.0
 */
public class CompactExample {
  private ByteBuffer buffer;

  @BeforeEach
  public void before() {
    buffer = ByteBuffer.wrap("12345678".getBytes());
  }

  @Test
  public void case1() {
    System.out.println((char) buffer.get());
    System.out.println((char) buffer.get());
    System.out.println("get() x 2=======,position()=" + buffer.position());
    buffer.compact();
    System.out.println(
        "compact after,position()=" + buffer.position() + ",limit()=" + buffer.limit());
    while (buffer.hasRemaining()) {
      System.out.print((char) buffer.get());
    }
    System.out.println();
  }

  @Test
  public void case2() {
    buffer.limit(6);
    System.out.println((char) buffer.get());
    System.out.println((char) buffer.get());
    System.out.println((char) buffer.get());
    System.out.println((char) buffer.get());
    System.out.println((char) buffer.get());
    System.out.println((char) buffer.get());
    System.out.println("get() x 6=======,position()=" + buffer.position());
    buffer.compact();
    System.out.println(
        "compact after,position()=" + buffer.position() + ",limit()=" + buffer.limit());
    while (buffer.hasRemaining()) {
      System.out.print((char) buffer.get());
    }
    System.out.println();
    System.out.println(new String(buffer.array(), 0, buffer.limit()));
  }

  @Test
  public void case3() throws IOException {
    // 内容：0~9 x 4
    final ReadableByteChannel channel1 =
        Channels.newChannel(
            new FileInputStream(FileUtil.getBuildPath("experiment/buffer/CompactExample.txt")));
    final ByteBuffer buffer = ByteBuffer.allocate(12);
    channel1.read(buffer);

    buffer.rewind();
    for (int i = 0; i < 10; i++) {
      System.out.println((char) buffer.get());
    }
    // 0~9，这里还有 0、1没输出
    System.out.println();
    //    buffer.clear(); 由于上一次 buffer的数据还没加载完，我们clear然后又读，此时 0、1就漏掉了
    // 要解决这个问题，compact出马，将未读的数据：0、1保留到下一次read，position指向 2，然后调用read方法不会 让0、1从数组里面出去！
    buffer.compact();
    channel1.read(buffer);
    buffer.rewind();

    for (int i = 0; i < 10; i++) {
      System.out.println((char) buffer.get());
    }
  }
}
