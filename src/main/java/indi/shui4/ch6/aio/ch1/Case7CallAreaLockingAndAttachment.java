package indi.shui4.ch6.aio.ch1;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/**
 * 6.1.7 执行指定范围的锁定与传入附件及整合接口
 *
 * @author shui4
 * @since 1.0
 */
public class Case7CallAreaLockingAndAttachment {

  public static void main(String[] args) {

    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(Paths.get("aio/a.txt"), StandardOpenOption.WRITE)) {

      channel.lock(
          0, 3, false, "我是附件", new Case6CompletionHandlerFailed.CompletionHandlerImpl(channel));
      TimeUnit.SECONDS.sleep(3);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
