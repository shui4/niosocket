package indi.shui4.ch6.aio.ch2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 接受方式1
 *
 * @author shui4
 * @since 1.0
 */
public class Case1Accept {

  @Test
  public void server() throws InterruptedException {
    try (AsynchronousServerSocketChannel channel =
        AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8088))) {
      channel.accept(
          null,
          new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(final AsynchronousSocketChannel result, final Void attachment) {
              channel.accept(null, this);
              System.out.println("completed threadName=" + Thread.currentThread().getName());
              final ByteBuffer buffer = ByteBuffer.allocate(20);
              final Future<Integer> readFuture = result.read(buffer);
              try {
                System.out.println(new String(buffer.array(), 0, readFuture.get()));
              } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
              System.out.println("failed");
              System.out.println(exc.getClass().getName());
            }
          });
      while (true) {
        TimeUnit.SECONDS.sleep(1);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client1() {
    try (Socket socket = new Socket("localhost", 8088)) {
      socket.getOutputStream().write("我来自客户端！".getBytes());
      Thread.sleep(1000);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client2() {
    try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
      channel.connect(
          new InetSocketAddress("localhost", 8088),
          null,
          new CompletionHandler<Void, Void>() {
            @Override
            public void completed(final Void result, final Void attachment) {
              System.out.println(Thread.currentThread().getName());
              final Future<Integer> future = channel.write(ByteBuffer.wrap("我来自客户端2".getBytes()));
              try {
                System.out.println("写入大小：" + future.get());
              } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {}
          });
      TimeUnit.SECONDS.sleep(10);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
