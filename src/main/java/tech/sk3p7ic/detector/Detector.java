package tech.sk3p7ic.detector;

import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;
import tech.sk3p7ic.detector.detection.SourceFormatter;
import tech.sk3p7ic.detector.files.FileIndexPair;
import tech.sk3p7ic.detector.files.FileIndexType;
import tech.sk3p7ic.detector.files.SourceFile;

import java.io.File;
import java.util.List;

public class Detector {
  private final static SimilarityScoreManager scoreManager = new SimilarityScoreManager();
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

  public SimilarityScoreManager generateSimilarityScores()
          throws IllegalArgumentException {
    SourceFormatter sourceFormatter = new SourceFormatter();
    file1.generateAll(); // Generate all pairs for first file
    List<FileIndexPair> pair1 = file1.getFileIndexPairs(); // Get the first pair
    sourceFormatter.formatSourceInputList(pair1); // Remove comments and change variables to a more standard format
    file2.generateAll(); // Generate all pairs for second file
    List<FileIndexPair> pair2 = file2.getFileIndexPairs(); // Get the second pair
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
    return scoreManager;
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
    //int totalLines = larger.indexEnd - larger.indexStart;
    //int totalLines = (larger.indexEnd - larger.indexStart) - 1;
    int totalLines = larger.content.size();
    int numMatches = 0;
    for (int i = 0; i < smaller.content.size(); i++) {
      //if (smaller.content.get(smaller.indexStart + i).equals(larger.content.get(larger.indexStart + i))) numMatches++;
      String[] smallerPairLine = smaller.content.get(smaller.indexStart + i).split(" ");
      String[] largerPairLine = larger.content.get(larger.indexStart + i).split(" ");
      int lineScore = 0;
      // Sanity check for if both lines are empty
      if (smallerPairLine.length == 0 && largerPairLine.length == 0) numMatches++;
      if (smallerPairLine.length > largerPairLine.length) {
        for (int j = 0; j < largerPairLine.length; j++) {
          if (smallerPairLine[j].equals(largerPairLine[j])) lineScore++;
        }
        if (lineScore != 0)
          if ((float) (largerPairLine.length / lineScore) >= 0.75f) numMatches++;
      } else {
        for (int j = 0; j < smallerPairLine.length; j++) {
          if (smallerPairLine[j].equals(largerPairLine[j])) lineScore++;
        }
        if (lineScore != 0)
          if ((float) (smallerPairLine.length / lineScore) >= 0.75f) numMatches++;
      }
    }
    return (totalLines == 0) ? 0.0f : ((float) numMatches / (float) totalLines);
  }
}
