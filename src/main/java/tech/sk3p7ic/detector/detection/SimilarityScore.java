package tech.sk3p7ic.detector.detection;


import tech.sk3p7ic.detector.files.FileIndexPair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimilarityScore {
  public final int scoreId;
  public final float similarityScore;
  public final File sourceFile1;
  public final File sourceFile2;
  public final FileIndexPair fileIndexPair1;
  public final FileIndexPair fileIndexPair2;

  /**
   * Stores a similarity score for a given block of code with a given file and given indexes.
   *
   * @param scoreId         The ID of the score.
   * @param similarityScore The similarity score itself.
   * @param sourceFile1     The first source file for the given score.
   * @param sourceFile2     The second source file for the given score.
   * @param fileIndexPair1  The first set of file index information for the given score.
   * @param fileIndexPair2  The second set of file index information for the given score.
   */
  public SimilarityScore(int scoreId, float similarityScore, File sourceFile1, File sourceFile2,
                         FileIndexPair fileIndexPair1, FileIndexPair fileIndexPair2) {
    this.scoreId = scoreId;
    this.similarityScore = similarityScore;
    this.sourceFile1 = sourceFile1;
    this.sourceFile2 = sourceFile2;
    this.fileIndexPair1 = fileIndexPair1;
    this.fileIndexPair2 = fileIndexPair2;
  }

  /**
   * Gets the lines of code that the similarity score is for.
   *
   * @return The lines of code that the similarity score is for.
   *
   * @throws IOException If the source file cannot be read.
   */
  public List<Map<Integer, String>> showSimilarLines() throws IOException {
    List<Map<Integer, String>> mapList = new ArrayList<>(); // Stores the two maps containing the lines of code
    // NOTE: Extra maps must be used since FileIndexPair.content has modified maps of the code without comments and with
    // Different variable names
    Map<Integer, String> file1Lines = new LinkedHashMap<>(); // Stores the lines of code from the first file
    Map<Integer, String> file2Lines = new LinkedHashMap<>(); // Stores the lines of code from the second file
    List<String> sourceFile1Content = Files.readAllLines(sourceFile1.toPath()); // Get the content of the first file
    List<String> sourceFile2Content = Files.readAllLines(sourceFile2.toPath()); // Get the content of the second file
    // Read the lines of code that the similarity score is for an add to map
    for (int i = fileIndexPair1.indexStart; i < fileIndexPair1.indexEnd; i++)
      file1Lines.put(i, sourceFile1Content.get(i));
    // Read the lines of code that the similarity score is for an add to map
    for (int i = fileIndexPair2.indexStart; i < fileIndexPair2.indexEnd; i++)
      file2Lines.put(i, sourceFile2Content.get(i));
    // Add the maps and return
    mapList.add(file1Lines);
    mapList.add(file2Lines);
    return mapList;
  }
}
