package indi.shui4.filechannel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("all")
public class Example13_Lock {


  // 本实验要在2个进程中进行测试，所以要创建2个Java文件。
  @Test
  public void case1_1() {
    try (FileChannel channel = getChannel()) {
      System.out.println("A begin");
      channel.lock(1, 2, false);
      System.out.println("A end");
      try {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 先执行case1，再执行这个，发现这里获取不到lock，原因是 上面加了一个独占锁 */
  @Test
  public void case1_2() {
    try (FileChannel channel = getChannel()) {
      System.out.println("A begin");
      channel.lock(1, 2, false);
      System.out.println("A end");
      try {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 验证AsynchronousCloseException 异常的发生 */
  @Test
  public void case3() {
    try (FileChannel fileChannel = getChannel()) {

      final Thread a =
          new Thread(
              () -> {
                try {
                  fileChannel.lock(1, 2, false);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });

      final Thread b =
          new Thread(
              () -> {
                try {
                  fileChannel.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });

      a.start();
      Thread.sleep(1);
      b.start();
      TimeUnit.SECONDS.sleep(1);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  // <editor-fold desc="4.验证FileLockInterruptionException（case4~5）">
  /**
   * 验证FileLockInterruptionException 异常的发生 <br>
   * 在一个线程 lock之后，被调用了 interrupt出现，也就是说他能够感知中断
   */
  @Test
  public void case4() {
    try (FileChannel channel = getChannel()) {
      final Thread t1 =
          new Thread(
              () -> {
                for (int i = 0; i < 1_000_000; i++) {
                  System.out.println("i=" + (i + 1));
                }

                try {
                  channel.lock(1, 2, false);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });

      t1.start();
      Thread.sleep(50);
      t1.interrupt();
      TimeUnit.SECONDS.sleep(30);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case5_1() {
    try (FileChannel channel = getChannel()) {
      System.out.println("A begin");
      channel.lock(0, 2, false);

      System.out.println("A end");
      TimeUnit.SECONDS.sleep(20);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * 由于上面方法优先执行，这里的线程无法获取锁，在20秒之后出现异常
   * FileLockInterruptionException，需要注意的是这里不会想Thread.sleep那样马上感知，只有在获取到锁的时候才会去看自己是不是被打断了，也就是这里需要等上面的20秒把锁释放掉才行
   */
  @Test
  public void case5_2() {
    final Thread t1 =
        new Thread(
            () -> {
              try (FileChannel channel = getChannel()) {
                System.out.println("B begin");
                channel.lock(0, 2, false);
                System.out.println("B end");
              } catch (FileNotFoundException e) {
                e.printStackTrace();
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    t1.start();
    try {
      Thread.sleep(2000);
      t1.interrupt();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  /** 验证共享锁自己不能写（出现异常） */
  @Test
  public void case6() {
    try (FileChannel channel = getChannel()) {
      channel.lock(1, 2, true);
      channel.write(ByteBuffer.wrap("123456".getBytes()));

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // <editor-fold desc="5.验证共享锁别人不能写（出异常）（case7）">
  @Test
  public void case7_1() {
    try (FileChannel channel = getChannel()) {
      channel.lock(1, 2, true);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case7_2() {
    try (FileChannel channel = getChannel()) {
      channel.write(ByteBuffer.wrap("123456".getBytes()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  // <editor-fold desc="8.验证共享锁自己能读">
  @Test
  public void case9() {
    try (FileChannel channel = getChannel()) {
      channel.lock(1, 2, true);
      final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
      channel.read(byteBuffer);
      byteBuffer.remaining();
      for (int i = 0; i < byteBuffer.limit(); i++) {
        System.out.println((char) byteBuffer.get());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  // <editor-fold desc="7.验证共享锁别人能读（case10~11）">
  @Test
  public void case10_1() {
    try (FileChannel channel = getChannel()) {
      channel.lock(1, 2, true);
      try {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case10_2() {
    try (FileChannel fileChannel = getChannel()) {
      final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
      fileChannel.read(byteBuffer);
      byteBuffer.rewind();
      for (int i = 0; i < byteBuffer.limit(); i++) {
        System.out.println((char) byteBuffer.get());
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  /** 验证独占锁自己能写 */
  @Test
  public void case12() {
    try (FileChannel channel = getChannel()) {
      channel.lock(1, 2, false);
      channel.write(ByteBuffer.wrap("123456".getBytes()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // <editor-fold desc="9.验证独占锁别人不能写（出现异常）（13、14）">
  @Test
  public void case13_1() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.lock(1, 2, false);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case13_2() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.write(ByteBuffer.wrap("123456".getBytes()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  /** 独占锁自己能读 */
  public void case14() {
    try (FileChannel channel = getChannel()) {
      channel.lock(1, 2, false);
      final ByteBuffer buffer = ByteBuffer.allocate(10);
      channel.read(buffer);
      buffer.rewind();
      for (int i = 0; i < buffer.limit(); i++) {
        System.out.print((char) buffer.get());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // <editor-fold desc="11.验证独占锁别人不能读（出现异常）（15、16）">
  @Test
  public void case15_1() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.lock(1, 2, false);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case15_2() {
    try (FileChannel fileChannel = getChannel()) {
      final ByteBuffer buffer = ByteBuffer.allocate(10);
      fileChannel.read(buffer);
      buffer.rewind();
      for (int i = 0; i < buffer.limit(); i++) {
        System.out.print((char) buffer.get());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  /** 验证ock()方法的参数position和size的含义 */
  @Test
  public void case16() {
    try (FileChannel channel = getChannel()) {
      System.out.println("A " + channel.position());
      // 必须使用共享锁
      channel.lock(3, 2, true);
      System.out.println("B " + channel.position());
      channel.write(ByteBuffer.wrap("1".getBytes()));
      System.out.println("C " + channel.position());
      channel.write(ByteBuffer.wrap("2".getBytes()));
      System.out.println("D " + channel.position());
      channel.write(ByteBuffer.wrap("3".getBytes()));
      System.out.println("E " + channel.position() + " 在position为3处write()就出现异常了");
      channel.write(ByteBuffer.wrap("4".getBytes()));
      System.out.println("F " + channel.position());
      channel.write(ByteBuffer.wrap("5".getBytes()));
      System.out.println("G " + channel.position());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 提前锁：当前文件大小小于lock方法参数设置的position时，是可以提前在position位置处加锁的 */
  @Test
  public void case17() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.lock(6, 2, true);
      fileChannel.write(ByteBuffer.wrap("1".getBytes()));
      fileChannel.write(ByteBuffer.wrap("2".getBytes()));
      fileChannel.write(ByteBuffer.wrap("3".getBytes()));
      fileChannel.write(ByteBuffer.wrap("4".getBytes()));
      fileChannel.write(ByteBuffer.wrap("5".getBytes()));
      fileChannel.write(ByteBuffer.wrap("6".getBytes()));
      // 此处实现异常
      // 文件内容：1~6
      fileChannel.write(ByteBuffer.wrap("7".getBytes()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // <editor-fold desc="14.验证共享锁与共享锁之间是非互斥关系">
  @Test
  public void case18_1() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.lock(0, Long.MAX_VALUE, true);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case18_2() {
    try (FileChannel fileChannel = getChannel()) {
      System.out.println("begin");
      fileChannel.lock(0, Long.MAX_VALUE, true);
      System.out.println(" end 拿到锁了");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  // <editor-fold desc="15.验证共享锁与独享锁之间是否互斥关系">
  @Test
  public void case19_1() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.lock(0, Long.MAX_VALUE, true);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case19_2() {
    try (FileChannel fileChannel = getChannel()) {
      System.out.println("begin");
      fileChannel.lock(0, Long.MAX_VALUE, false);
      System.out.println(" end 拿到锁了");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>

  // <editor-fold desc="16.验证独享锁与共享锁是互斥关系">
  @Test
  public void case20_1() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.lock(0, Long.MAX_VALUE, false);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void case20_2() {
    try (FileChannel fileChannel = getChannel()) {
      System.out.println("begin");
      fileChannel.lock(0, Long.MAX_VALUE, true);
      System.out.println(" end 拿到锁了");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>
  
  
  
  // <editor-fold desc="17.验证独享锁与独享锁是互斥关系">
  @Test
  public void case21_1() {
    try (FileChannel fileChannel = getChannel()) {
      fileChannel.lock(0, Long.MAX_VALUE, false);
      Thread.sleep(Integer.MAX_VALUE);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void case21_2() {
    try (FileChannel fileChannel = getChannel()) {
      System.out.println("begin");
      fileChannel.lock(0, Long.MAX_VALUE, false);
      System.out.println(" end 拿到锁了");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // </editor-fold>
  
  private FileChannel getChannel() throws FileNotFoundException {
    return new RandomAccessFile(FileUtil.getBuildPath("filechannel/13/a.txt"), "rw").getChannel();
  }
}
