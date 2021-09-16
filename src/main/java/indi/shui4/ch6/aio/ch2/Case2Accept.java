package indi.shui4.ch6.aio.ch2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class Case2Accept {

  @Test
  public void server() {
    try (AsynchronousServerSocketChannel channel =
        AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8088))) {
      System.out.println("A " + System.currentTimeMillis());
      final Future<AsynchronousSocketChannel> socketChannelFuture = channel.accept();
      System.out.println("B " + System.currentTimeMillis());
      try {
        final AsynchronousSocketChannel socketChannel = socketChannelFuture.get();
        System.out.println("C " + System.currentTimeMillis());
        final ByteBuffer buffer = ByteBuffer.allocate(20);
        System.out.println("D " + System.currentTimeMillis());
        final Future<Integer> readFuture = socketChannel.read(buffer);
        System.out.println("E " + System.currentTimeMillis());
        System.out.println(new String(buffer.array(), 0, readFuture.get()));
        System.out.println("F " + System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(40);
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@link Case1Accept#client1()} */
  @Test
  public void client1() {
    new Case1Accept().client1();
  }

  @Test
  public void client2() {
    new Case1Accept().client2();
  }
}
