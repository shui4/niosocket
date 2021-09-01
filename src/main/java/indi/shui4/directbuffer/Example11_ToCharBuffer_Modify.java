package indi.shui4.directbuffer;

import indi.shui4.util.BufferUtil;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 转换为CharBuffer+修改
 *
 * @author shui4
 * @since 2021/9/1(1.0)
 */
public class Example11_ToCharBuffer_Modify {

  public static void main(String[] args) throws UnsupportedEncodingException {
    // ByteBufferAsCharBufferB的get方法使用的是UTF_16,因此这里要一致
    final byte[] byteArrayIn = "你好".getBytes(StandardCharsets.UTF_16);
    final ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayIn);
    final CharBuffer charBuffer = byteBuffer.asCharBuffer();
    System.out.println(BufferUtil.getContent(byteBuffer));
    // 修改CharBuffer，ByteBuffer跟着变
    charBuffer.put(1, '他');
    System.out.println(BufferUtil.getContent(byteBuffer));
    System.out.println(BufferUtil.getContent(charBuffer));
    // 修改ByteBuffer，CharBuffer跟着变
    byteBuffer.put(1, (byte) 98);
    System.out.println(BufferUtil.getContent(byteBuffer));
    System.out.println(BufferUtil.getContent(charBuffer));
  }
}
