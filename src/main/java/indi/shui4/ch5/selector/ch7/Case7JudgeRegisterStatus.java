package indi.shui4.ch5.selector.ch7;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * 5.7.7 判断注册的状态
 *
 * @author shui4
 * @since 1.0
 */
public class Case7JudgeRegisterStatus {

  public static void main(String[] args) {
    try (final ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.configureBlocking(false);
      channel.socket().bind(new InetSocketAddress(8888));
      System.out.println("A isRegistered()=" + channel.isRegistered());
      try (final Selector selector = Selector.open()) {
        SelectionKey key = channel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("B isRegistered()=" + channel.isRegistered());
        key.cancel();
        System.out.println("C isRegistered()=" + channel.isRegistered());
        ThreadUtil.sleep(4, TimeUnit.SECONDS);
        System.out.println("D isRegistered()=" + channel.isRegistered());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
