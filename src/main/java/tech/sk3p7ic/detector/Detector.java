package tech.sk3p7ic.detector;

import tech.sk3p7ic.detector.detection.SimilarityScoreManager;
import tech.sk3p7ic.detector.detection.SourceFormatter;
import tech.sk3p7ic.detector.files.FileIndexPair;
import tech.sk3p7ic.detector.files.FileIndexType;
import tech.sk3p7ic.detector.files.SourceFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detector {
  private static SimilarityScoreManager scoreManager = new SimilarityScoreManager();
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
    SourceFormatter sourceFormatter = new SourceFormatter();
    List<FileIndexPair> pair1 = pairs[0]; // Get the first pair
    sourceFormatter.formatSourceInputList(pair1); // Remove comments and change variables to a more standard format
    List<FileIndexPair> pair2 = pairs[1]; // Get the second pair
    sourceFormatter.formatSourceInputList(pair2); // Remove comments and change variables to a more standard format
    // For each type, search the given pairs for all elements of that type and generate a similarity score.
    for (FileIndexType fileIndexType : FileIndexType.values()) {
      for (FileIndexPair indexPair1 : pair1) {
        if (indexPair1.fileIndexType != fileIndexType) continue;
        for (FileIndexPair indexPair2 : pair2) {
          if (indexPair2.fileIndexType != fileIndexType) continue;
          scoreManager.addSimilarityScore(getScore(indexPair1, indexPair2), file1.getSourceFile(),
              file2.getSourceFile(), indexPair1, indexPair2);
        }
      }
    }
    return new HashMap<>();
  }

  private float getScore(FileIndexPair indexPair1, FileIndexPair indexPair2) {
    FileIndexPair larger, smaller; // Stores which file has more lines
    // Find which pair has more / less lines and store as larger / smaller
    if (indexPair1.content.size() > indexPair2.content.size()) {
      larger = indexPair1;
      smaller = indexPair2;
    } else {
      larger = indexPair2;
      smaller = indexPair1;
    }
    int totalLines = larger.indexEnd - larger.indexStart;
    int numMatches = 0;
    for (int i = 0; i < smaller.content.size(); i++) {
      if (smaller.content.get(i).equals(larger.content.get(i))) numMatches++;
    }
    return (float) (numMatches / totalLines);
    //return 0; // TODO: Generate similarity scores.
  }
}
