package tech.sk3p7ic.detector;

import tech.sk3p7ic.detector.files.FileIndexPair;
import tech.sk3p7ic.detector.files.FileIndexType;
import tech.sk3p7ic.detector.files.SourceFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detector {
  private SourceFile file1;
  private SourceFile file2;

  private Detector() {
  }

  public Detector(SourceFile file1, SourceFile file2) {
    this.file1 = file1;
    this.file2 = file2;
  }

  public Detector(File file1, File file2) {
    this(new SourceFile(file1), new SourceFile(file2));
  }

  /**
   * Calls the SourceFile methods to read the files and get the various items contained within them.
   *
   * @return A list of List<FileIndexPair> of size 2 for file1 and file2, respectively.
   */
  public List[] readFilesForItems() {
    file1.generateAll();
    file2.generateAll();
    return new List[]{file1.getFileIndexPairs(), file2.getFileIndexPairs()};
  }

  public Map<FileIndexPair, Integer> generateSimilarityScores(List<FileIndexPair>[] pairs)
          throws IllegalArgumentException {
    if (pairs.length != 2)
      throw new IllegalArgumentException("Size of pairs does not equal 2: (" + pairs.length + ")..!");
    List<FileIndexPair> pair1 = pairs[0];
    List<FileIndexPair> pair2 = pairs[1];
    // For each type, search the given pairs for all elements of that type and generate a similarity score.
    for (FileIndexType fileIndexType : FileIndexType.values()) {
      System.out.println(fileIndexType);
    }
    return new HashMap<>();
  }
}
