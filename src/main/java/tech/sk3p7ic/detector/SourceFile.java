package tech.sk3p7ic.detector;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SourceFile {
  private File sourceFile;

  private SourceFile() {}

  public SourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  public SourceFile(String filePath) {
    this(new File(filePath));
  }

  public Map<Integer, String> getClassFromFile() {
    FileReader reader = new FileReader(sourceFile);
    try {
      return reader.getClassFromFile();
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
      return new LinkedHashMap<>();
    }
  }
}
