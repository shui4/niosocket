package indi.shui4.ch5.selector.ch7;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.nio.channels.SelectionKey.*;

/**
 * 5.7.23 类 FileChannel 中的 transferTo 方法使用
 *
 * @author shui4
 * @since 1.0
 */
public class Case23TransferToMethodInFileChannel {
  @Test
  public void server() {
    SocketChannel socketChannel = null;
    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress("localhost", 8088));
      final Selector selector = Selector.open();
      serverSocketChannel.register(selector, OP_ACCEPT);

      boolean isRun = true;
      while (isRun) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          final SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, OP_WRITE);
          }
          if (key.isWritable()) {
            try (RandomAccessFile file =
                    new RandomAccessFile(FileUtil.getBuildPath("nio/23/a.txt"), "rw");
                FileChannel fileChannel = file.getChannel()) {
              System.out.println("file.length()=" + file.length());
              fileChannel.transferTo(0, file.length(), socketChannel);
              socketChannel.close();
            }
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.connect(new InetSocketAddress("localhost", 8088));
      final Selector selector = Selector.open();
      socketChannel.register(selector, OP_CONNECT);
      boolean isRun = true;
      while (isRun) {
        System.out.println("begin selector");
        if (!socketChannel.isOpen()) {
          break;
        }
        selector.select();
        System.out.println("end selector");
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          final SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isConnectable()) {
            while (!socketChannel.finishConnect()) {}
            socketChannel.register(selector, OP_READ);
          }

          if (key.isReadable()) {
            final ByteBuffer byteBuffer = ByteBuffer.allocate(50_000);
            int readLength;
            int count = 0;

            try (FileChannel fileChannel =
                new FileOutputStream(FileUtil.getBuildPath("nio/23/b.txt")).getChannel()) {
              while ((readLength = socketChannel.read(byteBuffer)) != -1) {
                count += readLength;
                System.out.println("count=" + count + ",readLength=" + readLength);
                byteBuffer.flip();
                fileChannel.write(byteBuffer);
                byteBuffer.rewind();
              }
              System.out.println("读取结束");
              System.out.println();
              socketChannel.close();
            }
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
