package tech.sk3p7ic.detector;

import tech.sk3p7ic.detector.files.SourceFile;

import java.io.File;

public class Detector {
  private SourceFile file1;
  private SourceFile file2;

  private Detector() {}

  public Detector(SourceFile file1, SourceFile file2) {
    this.file1 = file1;
    this.file2 = file2;
  }

  public Detector(File file1, File file2) {
    this(new SourceFile(file1), new SourceFile(file2));
  }
}
