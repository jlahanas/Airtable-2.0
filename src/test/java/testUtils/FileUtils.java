package testUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
  public static String readJsonFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return Files.readString(path);
  }
}
