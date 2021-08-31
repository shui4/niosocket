package indi.shui4.directbuffer;

import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * 堆 vs 直接 内存操作耗时比较
 *
 * @author xdc
 * @since 2021/8/31(1.0)
 */
public class Example2_HeapVSDirectOpt {

  private static final int CAPACITY = 2000000000;

  @Test
  public void direct() {
    // 1.341 s
    run(() -> ByteBuffer.allocateDirect(CAPACITY));
  }

  @Test
  public void heap() {
    // 1.966 s
    run(() -> ByteBuffer.allocate(CAPACITY));
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
