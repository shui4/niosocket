package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * 5.7.10 判断打开的状态
 *
 * @author shui4
 * @since 1.0
 */
public class Case10JudgeOpenStatus {
  public static void main(String[] args) {
    try {
      ServerSocketChannel channel = ServerSocketChannel.open();
      System.out.println("A isOpen=" + channel.isOpen());
      channel.close();
      System.out.println("B isOpen=" + channel.isOpen());
      channel = ServerSocketChannel.open();
      System.out.println("C isOpen=" + channel.isOpen());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
