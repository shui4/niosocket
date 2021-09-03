package indi.shui4.util;

import java.nio.file.Paths;

/**
 * @author shui4
 * @since 2021/9/3(1.0)
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {

  public static String getCurrentProjectPath() {
    return Paths.get("").toAbsolutePath().toString();
  }

  public static String getBuildPath(String path) {
    return getCurrentProjectPath() + "\\" + path;
  }
}
