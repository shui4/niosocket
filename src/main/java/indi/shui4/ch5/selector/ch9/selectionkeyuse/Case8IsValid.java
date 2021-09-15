package indi.shui4.ch5.selector.ch9.selectionkeyuse;

import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.9.8 判断此键是否有效
 *
 * @author shui4
 * @since 1.0
 */
public class Case8IsValid {

  public static void main(String[] args) {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      final SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      System.out.println(key.isValid());
      // 这里 channel.close() 和 key.cancel() 都会使得key是无效的
      //      key.cancel();
      serverSocketChannel.close();
      System.out.println(key.isValid());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
