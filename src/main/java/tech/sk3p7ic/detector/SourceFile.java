package tech.sk3p7ic.detector;

import java.io.File;

public class SourceFile {
  private File sourceFile;

  private SourceFile() {}

  public SourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  public SourceFile(String filePath) {
    this(new File(filePath));
  }
}
