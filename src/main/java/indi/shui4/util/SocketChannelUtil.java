package indi.shui4.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author shui4
 * @since 1.0
 */
public class SocketChannelUtil {

  public static ServerSocketChannel getServerSocketChannel(int port, Selector selector)
      throws IOException {
    final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.bind(new InetSocketAddress(port));
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    return serverSocketChannel;
  }
}
