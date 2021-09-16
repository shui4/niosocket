package indi.shui4.ch6.aio.ch2;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 6.2.4 读数据
 *
 * @author shui4
 * @since 1.0
 */
public class Case4ReadData {
  /** 正常传输 */
  @Test
  public void client1() {
    try {
      final AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
      channel.connect(
          new InetSocketAddress("localhost", 8088),
          null,
          new CompletionHandler<Void, Void>() {
            @Override
            public void completed(final Void result, final Void attachment) {
              final ByteBuffer buffer = ByteBuffer.allocate(Integer.MAX_VALUE / 100);
              for (int i = 0; i < Integer.MAX_VALUE / 100 - 3; i++) {
                buffer.put("1".getBytes());
              }
              buffer.put("end".getBytes());
              buffer.flip();
              int writeSum = 0;
              try {
                while ((writeSum += channel.write(buffer).get()) < buffer.limit()) {}
              } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
              }
              try {
                channel.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
              System.out.println("failed " + exc.getClass().getName());
            }
          });
      try {
        TimeUnit.SECONDS.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 验证出现读超时异常 */
  @Test
  public void client2() throws IOException {
    final AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
    channel.connect(
        new InetSocketAddress("localhost", 8088),
        null,
        new CompletionHandler<Void, Void>() {
          @Override
          public void completed(final Void result, final Void attachment) {
            try {
              channel.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }

          @Override
          public void failed(final Throwable exc, final Void attachment) {
            System.out.println("failed");
            exc.printStackTrace();
          }
        });
  }

  @Test
  public void server() {
    try (AsynchronousServerSocketChannel channel =
        AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8088))) {
      channel.accept(
          null,
          new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(final AsynchronousSocketChannel ch, final Void attachment) {
              // 递归，下次继续接受
              channel.accept(null, this);

              final ByteBuffer buffer = ByteBuffer.allocate(Integer.MAX_VALUE / 100);
              final Stopwatch stopwatch = Stopwatch.createStarted();
              //                            ch.read(buffer, 10, TimeUnit.SECONDS, null,
              // getReadHandler(ch,
              //               buffer));
              ch.read(buffer, 1, TimeUnit.MILLISECONDS, null, getReadHandler(ch, buffer));
              System.out.println("read耗时：" + stopwatch.stop());
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
              System.out.println("accept failed");
            }
          });
      while (true) {}
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private CompletionHandler<Integer, Void> getReadHandler(
      final AsynchronousSocketChannel ch, final ByteBuffer buffer) {
    return new CompletionHandler<Integer, Void>() {
      @Override
      public void completed(final Integer result, final Void attachment) {
        if (result == -1) {
          System.out.println("客户端没有传输数据就执行close了，到stream end");
        }
        if (result == buffer.limit()) {
          System.out.println("服务端获得客户端完整数据");
        }
        try {
          ch.close();
          System.out.println("服务端close");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void failed(final Throwable exc, final Void attachment) {
        System.out.println("read failed");
        System.out.println(exc.getClass().getName() + ":" + exc.getMessage());
        exc.printStackTrace();
      }
    };
  }
}
