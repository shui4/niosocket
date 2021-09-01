package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 转换为CharBuffer
 *
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example11_ToCharBuffer {

  public static void main(String[] args) throws UnsupportedEncodingException {
    // ByteBufferAsCharBufferB的get方法使用的是UTF_16,因此这里要一致
    final byte[] byteArrayIn = "你好".getBytes(StandardCharsets.UTF_16);
    final ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayIn);
    final CharBuffer charBuffer = byteBuffer.asCharBuffer();
    System.out.println(BufferUtil.getContent(charBuffer));
  }
}
