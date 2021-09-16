package indi.shui4.ch6.aio.ch2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 6.2.3 重复读与重复写出现异常
 *
 * @author shui4
 * @since 1.0
 */
public class Case3RepeatedReadingAndRepeatedWriting {
  /** ReadPendingException 异常 */
  /*对于重复写的话，WritePendingException异常*/

  @Test
  public void server() {
    try (AsynchronousServerSocketChannel channel =
        AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8088))) {
      final Future<AsynchronousSocketChannel> socketChannelFuture = channel.accept();
      final AsynchronousSocketChannel socketChannel = socketChannelFuture.get();
      final Future<Integer> readFuture1 = socketChannel.read(ByteBuffer.allocate(20));
      final Future<Integer> readFuture2 = socketChannel.read(ByteBuffer.allocate(20));

    } catch (IOException | ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client1() {
    new Case1Accept().client1();
  }

  @Test
  public void client2() {
    new Case1Accept().client2();
  }
}
