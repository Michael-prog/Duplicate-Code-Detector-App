package tech.sk3p7ic.detector.detection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sk3p7ic.detector.files.FileIndexPair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimilarityScoreManager {
  private final static Logger logger = LoggerFactory.getLogger(SimilarityScoreManager.class);
  private final List<SimilarityScore> similarityScoreList; // Stores the similarity scores
  private static int currentScoreID = -1; // Stores the current score ID

  public SimilarityScoreManager() {
    similarityScoreList = new ArrayList<>();
    currentScoreID++;
  }

  /**
   * Adds a similarity score to the list of score for a given block of code with a given file and given indexes.
   *
   * @param similarityScore The similarity score itself.
   * @param sourceFile1     The first source file for the given score.
   * @param sourceFile2     The second source file for the given score.
   * @param fileIndexPair1  The first set of file index information for the given score.
   * @param fileIndexPair2  The second set of file index information for the given score.
   */
  public void addSimilarityScore(float similarityScore, File sourceFile1, File sourceFile2, FileIndexPair fileIndexPair1,
                                 FileIndexPair fileIndexPair2) {
    // Add the score to the list
    similarityScoreList.add(new SimilarityScore(currentScoreID, similarityScore, sourceFile1, sourceFile2,
        fileIndexPair1, fileIndexPair2));
    currentScoreID++; // Update the current score
  }

  /**
   * Gets the lines of code that the similarity score with the given ID is for.
   *
   * @param scoreId The ID of the similarity score to check
   * @return The lines of code that the similarity score is for. Returns null if the lines could not be displayed due to
   * an error reading the source file.
   */
  public List<Map<Integer, String>> getSimilarityScoreLines(int scoreId) {
    try {
      return similarityScoreList.get(scoreId).showSimilarLines(); // Attempt to get lines from each file for the score
    } catch (IOException e) {
      logger.error("Could not get lines for score '" + scoreId + "': " + e.getMessage());
      return null;
    }
  }

  public List<FileIndexPair> getSimilarFileIndexPairs(int scoreID) {
    List<FileIndexPair> indexPairList = new ArrayList<>();
    indexPairList.add(similarityScoreList.get(scoreID).fileIndexPair1);
    indexPairList.add(similarityScoreList.get(scoreID).fileIndexPair2);
    return indexPairList;
  }

  public List<SimilarityScore> getSimilarityScoreList() {
    return similarityScoreList;
  }

  public List<SimilarityScore> filterScores(String filter, double filterValue) {
    List<SimilarityScore> resultsList = new ArrayList<>();
    for (SimilarityScore score : similarityScoreList) {
      switch(filter) {
        case "gt":
          if (score.similarityScore > filterValue) resultsList.add(score);
          break;
        case "lt":
          if (score.similarityScore < filterValue) resultsList.add(score);
          break;
        case "ge":
          if (score.similarityScore >= filterValue) resultsList.add(score);
          break;
        case "le":
          if (score.similarityScore <= filterValue) resultsList.add(score);
          break;
        case "eq":
          if (score.similarityScore == filterValue) resultsList.add(score);
          break;
        default:
          return null; // Return null if there was an incorrect filter supplied
      }
    }
    return resultsList; // Return the filtered list of scores
  }
}
