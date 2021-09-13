package indi.shui4.ch5.selector.ch8.selectoruse;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class Case7InReadyKeyListGetChannelTheData {

  @Test
  public void client() throws InterruptedException {
    try (Socket socket = new Socket(); ) {
      socket.setSendBufferSize(1);
      socket.connect(new InetSocketAddress("localhost", 8888));
      final OutputStream outputStream = socket.getOutputStream();
      outputStream.write("我是中国人，我来自客户端！".getBytes());
      TimeUnit.SECONDS.sleep(4);
      outputStream.write("我是中国人，我来自客户端！".getBytes());
      outputStream.write("我是中国人，我来自客户端！".getBytes());
      //      outputStream.write("abcdefg".getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void server() {
    try (Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(8888));
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      while (true) {
        selector.select();
        System.out.println("selector.keys().size()=" + selector.keys().size());
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          if (key.isAcceptable()) {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            //            read1(iterator, channel);
            try (SocketChannel socketChannel = channel.accept()) {
              final ByteBuffer buffer = ByteBuffer.allocate(1000);
              int readLength;
              while ((readLength = socketChannel.read(buffer)) != -1) {
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                int i = 0;
                while (buffer.hasRemaining()) {
                  bytes[i] = buffer.get();
                  i++;
                }
                System.out.println(new String(bytes, 0, readLength));
                buffer.clear();
              }
            }
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void read1(final Iterator<SelectionKey> iterator, final ServerSocketChannel channel)
      throws IOException {
    ServerSocket serverSocket = channel.socket();
    Socket socket = serverSocket.accept();
    InputStream inputStream = socket.getInputStream();
    final byte[] bytes = new byte[1000];
    int readLength;
    while ((readLength = inputStream.read(bytes)) != -1) {
      System.out.print(new String(bytes, 0, readLength));
    }
    inputStream.close();
    socket.close();
    iterator.remove();
  }
}
