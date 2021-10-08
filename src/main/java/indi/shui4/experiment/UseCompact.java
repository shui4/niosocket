package indi.shui4.experiment;

import indi.shui4.util.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * 0~9a~i
 *
 * @author shui4
 * @since 2021/9/18(1.0)
 */
public class UseCompact {

  public static void main(String[] args) throws IOException {
    final ReadableByteChannel channel1 =
        Channels.newChannel(new FileInputStream(FileUtil.getBuildPath("experiment/c.txt")));
    final ByteBuffer buffer = ByteBuffer.allocate(12);
    channel1.read(buffer);

    buffer.rewind();
    for (int i = 0; i < 10; i++) {
      System.out.println((char) buffer.get());
    }
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
/*
 *
 *
 * */
