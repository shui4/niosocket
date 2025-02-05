package indi.shui4.experiment.channel;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.function.Consumer;

import static java.nio.channels.FileChannel.MapMode.*;

/**
 * {@link FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}
 *
 * @author shui4
 * @since 1.0
 */
public class FileChannelMap {
  public static final String PREFIX_PATH = "experiment/channel";
  public static final String PATH = FileUtil.getBuildPath(PREFIX_PATH + "/a.txt");

  /** 文件被其它进程修改，map中能看到 */
  static class Case1 {

    @Test
    public void test1() {

      try (RandomAccessFile file = new RandomAccessFile(PATH, "rw");
          FileChannel channel = file.getChannel()) {
        channel.write(ByteBuffer.wrap("a".getBytes()));
        System.out.println();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void test2() {
      try (RandomAccessFile file =
              new RandomAccessFile(FileUtil.getBuildPath(PREFIX_PATH + "/a.txt"), "rw");
          FileChannel channel = file.getChannel()) {
        // 创建一个虚拟内存映射
        final MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 10);
        System.out.println("mappedByteBuffer.get()=" + ((char) mappedByteBuffer.get()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** MappedByteBuffer对文件的修改，其它进程看不到 */
  static class Case2 {

    @Test
    public void test1MappedByteBuffer() {
      try (RandomAccessFile file = new RandomAccessFile(PATH, "rw");
          FileChannel channel = file.getChannel()) {
        final MappedByteBuffer mappedByteBuffer = channel.map(READ_WRITE, 0, 10);
        System.out.println("position=" + mappedByteBuffer.position());
        mappedByteBuffer.put((byte) 'z');
        mappedByteBuffer.position(0);
        System.out.println((char) mappedByteBuffer.get());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Test
    public void test2OtherProcess() {
      try (RandomAccessFile file = new RandomAccessFile(PATH, "rw");
          FileChannel channel = file.getChannel()) {
        final ByteBuffer buffer = ByteBuffer.allocate(3);
        while (channel.read(buffer) != -1) {
          buffer.flip();
          System.out.print(new String(buffer.array(), 0, buffer.limit()));
          buffer.clear();
        }
        System.out.println();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** 在r模式中，READ_ONLY不能超过文件size，rw可以 */
  static class Case3 {
    @Test
    public void test1() {
      try (
      //          RandomAccessFile file = new RandomAccessFile(PREFIX_PATH + "/b.txt", "r");
      RandomAccessFile file = new RandomAccessFile(PREFIX_PATH + "/b.txt", "rw");
          FileChannel channel = file.getChannel()) {
        channel.map(READ_ONLY, 0, 1024);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** copy on write {@link FileChannel.MapMode#PRIVATE} */
  static class Case4 {
    @Test
    public void read() {
      try (RandomAccessFile file = new RandomAccessFile(PATH, "rw");
          FileChannel channel = file.getChannel()) {
        final MappedByteBuffer map = channel.map(PRIVATE, 0, 5);

        System.out.println((char) map.get());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    /** 普通读 */
    @Test
    public void write() {
      try (RandomAccessFile file = new RandomAccessFile(PATH, "rw");
          FileChannel channel = file.getChannel()) {
        final ByteBuffer buffer = ByteBuffer.wrap("z".getBytes());

        channel.write(buffer);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings({"ResultOfMethodCallIgnored", "StringOperationCanBeSimplified"})
  static class Case5 {

    @Test
    public void test() {

      Consumer<RandomAccessFile> consumer =
          (file) -> {
            final FileChannel channel = file.getChannel();
            try {
              System.out.println("begin");
              //              channel.write(ByteBuffer.wrap("This is file content".getBytes()));
              channel.write(ByteBuffer.wrap("1234567890".getBytes()));
              channel.write(ByteBuffer.wrap("----".getBytes()), 2000);
              final MappedByteBuffer ro = channel.map(READ_ONLY, 0, channel.size());
              final MappedByteBuffer rw = channel.map(READ_WRITE, 0, channel.size());
              final MappedByteBuffer cow = channel.map(PRIVATE, 0, channel.size());
              showBuffers(ro, rw, cow);
              System.out.println();

              // channel
              System.out.println("A channel");
              channel.write(ByteBuffer.wrap(" channelA ".getBytes()), 100);
              channel.force(true);
              showBuffers(ro, rw, cow);
              System.out.println();
              // rw
              System.out.println("rw");
              rw.position(200);
              rw.put(ByteBuffer.wrap("R/W ".getBytes()));
              showBuffers(ro, rw, cow);
              rw.force();
              System.out.println();
              // cow
              cowChange(ro, rw, cow);
              // channel
              System.out.println("B channel");
              channel.write(ByteBuffer.wrap(" channelB ".getBytes()), 100);
              channel.force(true);
              // cow会不会跟着改变，取决于操作系统，在win10中根据测试，如果cow没有更新过
              // ，是可见channel的修改，如果cow更新了，那就对channel修改不可见
              showBuffers(ro, rw, cow);
            } catch (IOException e) {
              e.printStackTrace();
            }
          };

      try {
        final File tempFile = File.createTempFile(" ", null);
        try (RandomAccessFile file = new RandomAccessFile(tempFile, "rw")) {
          consumer.accept(file);
          file.close();
          tempFile.delete();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private void showBuffers(
        final MappedByteBuffer ro, final MappedByteBuffer rw, final MappedByteBuffer cow) {
      dumpBuffer("R/O", ro);
      dumpBuffer("R/W", rw);
      dumpBuffer("COW", cow);
      System.out.println("");
    }

    private void cowChange(
        final MappedByteBuffer ro, final MappedByteBuffer rw, final MappedByteBuffer cow) {
      System.out.println("cow");
      cow.position(300);
      cow.put(ByteBuffer.wrap("COW ".getBytes()));
      showBuffers(ro, rw, cow);
      cow.force();
      System.out.println();
    }

    private void dumpBuffer(final String prefix, final MappedByteBuffer buffer) {
      System.out.print(prefix + ":' ");
      final int limit = buffer.limit();
      int nulls = 0;
      for (int i = 0; i < limit; i++) {
        final char c = (char) buffer.get(i);
        if (c == ' ') {
          nulls++;
          continue;
        }
        if (nulls != 0) {
          System.out.print("|[" + nulls + " nulls]|");
          nulls = 0;
        }
        System.out.print(c);
      }
      System.out.println("'");
    }
  }
}
