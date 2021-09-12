package indi.shui4.ch5.selector.ch7;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * 5.7.17 获得 SelectorProvider 对象
 *
 * @author shui4
 * @since 1.0
 */
public class Case17GetSelectorProvider {

  public static void main(String[] args) throws IOException {
    System.out.println(SelectorProvider.provider());
    System.out.println(ServerSocketChannel.open().provider());
  }
}
