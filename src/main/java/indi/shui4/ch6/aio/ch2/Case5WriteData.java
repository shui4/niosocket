package indi.shui4.ch6.aio.ch2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

/**
 * 6.2.5 写数据
 *
 * @author shui4
 * @since 1.0
 */
public class Case5WriteData {
  /** 客户端正常写 */
  @Test
  public void client1() {
    try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
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
              channel.write(
                  buffer,
                  1,
                  TimeUnit.SECONDS,
                  null,
                  new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(final Integer result, final Object attachment) {
                      try {
                        channel.close();
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }

                    @Override
                    public void failed(final Throwable exc, final Object attachment) {
                      System.out.println("write failed");
                    }
                  });
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
              System.out.println("connect failed");
            }
          });
      TimeUnit.SECONDS.sleep(5);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /** 写操作超时的客户端 */
  @Test
  public void client2() {
    try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
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

              channel.write(
                  buffer,
                  1,
                  TimeUnit.MILLISECONDS,
                  null,
                  new CompletionHandler<Integer, Void>() {
                    @Override
                    public void completed(final Integer result, final Void attachment) {
                      try {
                        channel.close();
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }

                    @Override
                    public void failed(final Throwable exc, final Void attachment) {
                      System.out.println("write failed");
                      exc.printStackTrace();
                    }
                  });
            }

            @Override
            public void failed(final Throwable exc, final Void attachment) {
              System.out.println("connect failed");
              exc.printStackTrace();
            }
          });
      TimeUnit.SECONDS.sleep(5);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
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
              // 递归
              channel.accept(null, this);
              final ByteBuffer buffer = ByteBuffer.allocate(10);
              ch.read(
                  buffer,
                  10,
                  TimeUnit.SECONDS,
                  null,
                  new CompletionHandler<Integer, Void>() {
                    @Override
                    public void completed(final Integer result, final Void attachment) {
                      if (result == -1) {
                        System.out.println("客户端没有传输数据就执行了close了，到stream end");
                      }

                      if (result == buffer.limit()) {
                        System.out.println("服务端获得客户端完整数据");
                      }

                      try {
                        ch.close();
                      } catch (IOException e) {

                        e.printStackTrace();
                      }
                      System.out.println("服务端close");
                    }

                    @Override
                    public void failed(final Throwable exc, final Void attachment) {
                      System.out.println("read failed");
                      exc.printStackTrace();
                    }
                  });
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
}
