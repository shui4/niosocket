package indi.shui4.ch5.selector.ch7;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * 5.7.3 执行绑定操作与设置 backlog {@link ServerSocketChannel#bind(java.net.SocketAddress, int)}
 *
 * @author shui4
 * @since 1.0
 */
public class Case3CallsBindAndSetBacklog {

  @Test
  public void server() {
    try (final ServerSocketChannel channel = ServerSocketChannel.open()) {
      final int backlog = 60;
      channel.bind(new InetSocketAddress(8888), backlog);
      try (ServerSocket serverSocket = channel.socket()) {
        ThreadUtil.sleep(5, TimeUnit.SECONDS);
        boolean runFlag = true;
        while (runFlag) {
          try (Socket socket = serverSocket.accept()) {}
        }
      }
      ThreadUtil.sleep(8, TimeUnit.SECONDS);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void client() {
    for (int i = 0; i < 100; i++) {
      try (final Socket socket = new Socket("localhost", 8888)) {
        System.out.println("客户端连接个数：" + i);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
