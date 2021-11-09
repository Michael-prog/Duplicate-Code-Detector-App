package tech.sk3p7ic.detector.files;

import java.util.Map;

public class FileIndexPair {
  public final FileIndexType fileIndexType;
  public final int indexStart;
  public final int indexEnd;
  public final Map<Integer, String> content;

  /**
   * Stores information about a given block of code.
   *
   * @param fileIndexType The FileIndexType for the block of code being stored.
   * @param indexStart    The starting index in the source file for this block of code.
   * @param indexEnd      The ending index in the source file for this block of code.
   * @param content       The code from the source file this FileIndexPair is for.
   */
  public FileIndexPair(FileIndexType fileIndexType, int indexStart, int indexEnd, Map<Integer, String> content) {
    this.fileIndexType = fileIndexType;
    this.indexStart = indexStart;
    this.indexEnd = indexEnd;
    this.content = content;
  }
}
