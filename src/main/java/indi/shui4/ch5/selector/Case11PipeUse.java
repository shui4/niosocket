package indi.shui4.ch5.selector;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 5.11 Pipe.SinkChannel Pipe.SourceChannel 类的使用
 *
 * @author shui4
 * @since 1.0
 */
public class Case11PipeUse {
  @Test
  public void case1() throws InterruptedException {
    try {
      final Pipe pipe = Pipe.open();
      final Pipe.SinkChannel sinkChannel = pipe.sink();
      final Pipe.SourceChannel sourceChannel = pipe.source();

      new Thread(
              () -> {
                try {
                  Thread.sleep(1000);
                  for (int i = 0; i < 5; i++) {
                    sinkChannel.write(ByteBuffer.wrap(("我来自客户端A " + (i + 1) + "\r\n").getBytes()));
                  }
                } catch (InterruptedException | IOException e) {
                  e.printStackTrace();
                }
              })
          .start();

      new Thread(
              () -> {
                try {
                  Thread.sleep(1000);
                  for (int i = 0; i < 5; i++) {
                    try {
                      sinkChannel.write(
                          ByteBuffer.wrap(("我来自客户端B " + (i + 1) + "\r\n").getBytes()));
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  }
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              })
          .start();
      Thread.sleep(3_000);
      sinkChannel.close();
      final ByteBuffer buffer = ByteBuffer.allocate(1000);
      int readLength;
      while ((readLength = sourceChannel.read(buffer)) != -1) {
        System.out.println(new String(buffer.array(), 0, readLength));
      }
      sourceChannel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
