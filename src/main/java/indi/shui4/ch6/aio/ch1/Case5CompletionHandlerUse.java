package indi.shui4.ch6.aio.ch1;

import sun.util.locale.provider.TimeZoneNameUtility;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.5 CompletionHandler 接口的使用
 *
 * @author shui4
 * @since 1.0
 */
public class Case5CompletionHandlerUse {

  static class CompletionHandlerImpl implements CompletionHandler<FileLock, String> {
    private AsynchronousFileChannel channel;
  
    public CompletionHandlerImpl(final AsynchronousFileChannel channel) {
      this.channel = channel;
    }
  
    @Override
    public void completed(final FileLock result, final String attachment) {
      System.out.println("B thread name "+Thread.currentThread().getName());
  
      System.out.println(
          "public void completed(final FileLock result, final String attachment) attachment="
              + attachment);
      try {
        result.release();
        channel.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void failed(final Throwable exc, final String attachment) {
      System.out.println(
          "public void failed(final Throwable exc, final String attachment) attachment="
              + attachment);
      System.out.println("getMessage=" + exc.getMessage());
    }
  }

  public static void main(String[] args) {
    System.out.println("A thread name "+Thread.currentThread().getName());
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {
      System.out.println("begin time=" + System.currentTimeMillis());
      channel.lock("我是附加值", new CompletionHandlerImpl(channel));
      System.out.println("end time=" + System.currentTimeMillis());
      TimeUnit.SECONDS.sleep(3);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
