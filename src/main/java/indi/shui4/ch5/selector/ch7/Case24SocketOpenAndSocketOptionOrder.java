package indi.shui4.ch5.selector.ch7;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 5.7.24 方法 SocketChannel open(SocketAddress remote) 与 SocketOption 的执行顺序
 *
 * @author shui4
 * @since 1.0
 */
public class Case24SocketOpenAndSocketOptionOrder {

  @Test
  public void server() {
    try (ServerSocket serverSocket = new ServerSocket(8088);
        Socket socket = serverSocket.accept(); ) {
      final InputStream inputStream = socket.getInputStream();
      final byte[] bytes = new byte[1024];
      int readLength;
      while ((readLength = inputStream.read(bytes)) != -1) {
        System.out.println(new String(bytes, 0, readLength));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 错误的客户端，通过抓包工具Win没有变成1234 */
  @Test
  public void case1ErrorClient() {
    try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8088))) {
      socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1234);
      socketChannel.write(ByteBuffer.wrap("我是中国人我来自客户端".getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 正确的客户端 */
  @Test
  public void case2ProperClient() {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1234);
      socketChannel.connect(new InetSocketAddress(8088));
      socketChannel.write(ByteBuffer.wrap("我是中国人我来自客户端".getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
