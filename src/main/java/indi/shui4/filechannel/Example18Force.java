package indi.shui4.filechannel;

import com.google.common.base.Stopwatch;
import indi.shui4.util.FileUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * {@link FileChannel#force(boolean)} <br>
 * 不加force：5.478 ms <br>
 * 加force：297.6 ms
 *
 * @author shui4
 * @since 1.0
 */
public class Example18Force {

  public static void main(String[] args) {
    try (FileChannel channel =
        new FileOutputStream(FileUtil.getBuildPath("filechannel/18/a.txt")).getChannel()) {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      for (int i = 0; i < 500; i++) {
        channel.write(ByteBuffer.wrap(("abcd" + i).getBytes()));
        channel.force(false);
      }
      stopwatch.stop();
      System.out.println(stopwatch);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
