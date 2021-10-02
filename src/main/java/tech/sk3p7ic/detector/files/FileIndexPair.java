package tech.sk3p7ic.detector.files;

import java.util.Map;

public class FileIndexPair {
  public final FileIndexType fileIndexType;
  public final int indexStart;
  public final int indexEnd;
  public final Map<Integer, String> content;

  public FileIndexPair(FileIndexType fileIndexType, int indexStart, int indexEnd, Map<Integer, String> content) {
    this.fileIndexType = fileIndexType;
    this.indexStart = indexStart;
    this.indexEnd = indexEnd;
    this.content = content;
  }
}
