package indi.shui4.ch5.selector.ch7;

import indi.shui4.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static java.nio.channels.SelectionKey.*;

/**
 * 5.7.25 传输大文件
 *
 * @author shui4
 * @since 1.0
 */
public class Case25TransferLargeFiles {

  @Test
  public void server() {
    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(8088));
      final Selector selector = Selector.open();
      serverSocketChannel.register(selector, OP_ACCEPT);
      while (true) {
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          final SelectionKey key = iterator.next();
          iterator.remove();

          if (key.isAcceptable()) {
            try {
              SocketChannel socketChannel = serverSocketChannel.accept();
              socketChannel.configureBlocking(false);
              socketChannel.register(selector, OP_WRITE);
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else if (key.isWritable()) {
            try (SocketChannel socketChannel = (SocketChannel) key.channel();
                FileInputStream fileInputStream =
                    new FileInputStream(FileUtil.getBuildPath("nio/25/big.txt"));
                final FileChannel fileChannel = fileInputStream.getChannel()) {
              final ByteBuffer buffer = ByteBuffer.allocate(524_288_000);
              while (fileChannel.position() < fileChannel.size()) {
                fileChannel.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining()) {
                  socketChannel.write(buffer);
                }
                buffer.clear();
              }
              System.out.println("结束写操作");
            } catch (FileNotFoundException e) {
              e.printStackTrace();
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
    try {
      final SocketChannel socketChannel = SocketChannel.open();
      socketChannel.configureBlocking(false);
      final Selector selector = Selector.open();
      socketChannel.connect(new InetSocketAddress(8088));
      socketChannel.register(selector, OP_CONNECT);
      while (true) {
        System.out.println("begin selector");
        if (!socketChannel.isOpen()) break;
        selector.select();
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          final SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isConnectable()) {
            while (!socketChannel.finishConnect()) {}
            socketChannel.register(selector, OP_READ);
          }
          if (key.isReadable()) {
            final ByteBuffer buffer = ByteBuffer.allocate(50_000);
            int readLength = socketChannel.read(buffer);
            buffer.flip();
            int count = 0;
            while (readLength != -1) {
              count += readLength;
              System.out.println("count=" + count + " readLength=" + readLength);
              readLength = socketChannel.read(buffer);
              buffer.clear();
            }
            System.out.println("读取结束");
            socketChannel.close();
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
