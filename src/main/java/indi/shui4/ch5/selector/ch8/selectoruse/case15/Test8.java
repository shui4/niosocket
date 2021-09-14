package indi.shui4.ch5.selector.ch8.selectoruse.case15;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * 8. 调用 Selector.close()方法删除全部键并且通道注销
 *
 * @author shui4
 * @since 1.0
 */
public class Test8 {

  public static void main(String[] args) throws IOException {

    final Selector selector = Selector.open();
    new Thread(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("调用java.nio.channels.Selector.close方法");
                selector.close();
              } catch (InterruptedException | IOException e) {
                e.printStackTrace();
              }
            })
        .start();

    final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.bind(new InetSocketAddress(8888));
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    final int keyCount = selector.select();
    final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
    while (iterator.hasNext()) {
      SelectionKey key = iterator.next();
      if (key.isAcceptable()) {
        final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        final Socket socket = channel.socket().accept();
        socket.close();
      }
    }
    System.out.println("main end!");
  }
}
