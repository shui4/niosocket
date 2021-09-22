package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.5 CompletionHandler 接口的使用
 *
 * @author shui4
 * @since 1.0
 */
public class Case6CompletionHandlerFailed {

  static class CompletionHandlerImpl implements CompletionHandler<FileLock, String> {
    private AsynchronousFileChannel channel;

    public CompletionHandlerImpl(final AsynchronousFileChannel channel) {
      this.channel = channel;
    }

    @Override
    public void completed(final FileLock result, final String attachment) {
      System.out.println("B thread name " + Thread.currentThread().getName());

      System.out.println(
          "public void completed(final FileLock result, final String attachment) attachment="
              + attachment);
      try {
        result.release();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void failed(final Throwable exc, final String attachment) {
      System.out.println("B thread name " + Thread.currentThread().getName());
      System.out.println("failed(...) attachment=" + attachment);
      System.out.println("getMessage=" + exc.getMessage());
      System.out.println("exc.getClass().getName()=" + exc.getClass().getName());
    }
  }

  public static void main(String[] args) {
    System.out.println("A thread name " + Thread.currentThread().getName());
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(
            Paths.get("aio/a.txt"), StandardOpenOption.WRITE, StandardOpenOption.READ)) {
      System.out.println("begin time=" + System.currentTimeMillis());
      channel.close();
      channel.lock("我是附加值", new CompletionHandlerImpl(channel));
      System.out.println("end time=" + System.currentTimeMillis());
      TimeUnit.SECONDS.sleep(3);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
