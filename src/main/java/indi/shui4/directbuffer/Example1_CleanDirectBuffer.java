package indi.shui4.directbuffer;

import com.google.common.base.Stopwatch;
import sun.misc.Cleaner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 2021/8/31(1.0)
 */
public class Example1_CleanDirectBuffer {

  public static void main(String[] args)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
          InterruptedException {
    List<ByteBuffer> list = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
      final ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 50);
      list.add(buffer);
    }
    System.out.println("加入成功");
    TimeUnit.SECONDS.sleep(5);
    Stopwatch stopwatch = Stopwatch.createStarted();
    for (ByteBuffer buffer : list) {
      clean(buffer);
    }
    stopwatch.stop();

    System.out.println("清理成功");
    TimeUnit.MILLISECONDS.sleep(100);
    System.out.println(stopwatch.toString());
  }

  private static void clean(ByteBuffer buffer)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    final Method cleanerMethod = buffer.getClass().getDeclaredMethod("cleaner");
    cleanerMethod.setAccessible(true);
    final Object obj = cleanerMethod.invoke(buffer);
    if (obj instanceof Cleaner) {
      Cleaner cleaner = (Cleaner) obj;
      cleaner.clean();
    }
  }
}
