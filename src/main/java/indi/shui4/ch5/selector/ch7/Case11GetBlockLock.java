package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.7.11 获得阻塞锁对象
 *
 * @author shui4
 * @since 1.0
 */
public class Case11GetBlockLock {
  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      System.out.println(channel.blockingLock());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
