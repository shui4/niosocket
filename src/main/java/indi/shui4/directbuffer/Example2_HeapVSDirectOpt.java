package indi.shui4.directbuffer;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

/**
 * 堆 vs 直接 内存操作耗时比较
 *
 * @author shui4
 * @since 2021/8/31(1.0)
 */
public class Example2_HeapVSDirectOpt {

  private static final int CAPACITY = 2000000000;

  @Test
  public void direct() {
    // 1.341 s
    run(
        () -> {
          final Stopwatch stopwatch = Stopwatch.createStarted();
          final ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY);
          stopwatch.stop();
          // 创建耗时 ->513.0 ms
          Console.log("创建耗时 ->{}", stopwatch);
          return buffer;
        });
  }

  @Test
  public void heap() {
    // 1.966 s
    run(
        () -> {
          final Stopwatch stopwatch = Stopwatch.createStarted();
          final ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
          stopwatch.stop();
          // 创建耗时 ->403.7 ms
          Console.log("创建耗时 ->{}", stopwatch);
          return buffer;
        });
  }

  private void run(Supplier<ByteBuffer> supplier) {
    final Stopwatch stopWatch = Stopwatch.createStarted();
    final ByteBuffer buffer = supplier.get();
    for (int i = 0; i < CAPACITY; i++) {
      buffer.put((byte) 123);
    }
    stopWatch.stop();
    System.out.println(stopWatch.toString());
  }
}
