package tech.sk3p7ic.detector.detection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sk3p7ic.detector.files.FileIndexPair;
import tech.sk3p7ic.detector.files.FileIndexType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SimilarityScoreManager {
  private final static Logger logger = LoggerFactory.getLogger(SimilarityScoreManager.class);
  private static int currentScoreID = -1; // Stores the current score ID
  private final List<SimilarityScore> similarityScoreList; // Stores the similarity scores

  /**
   * Manages a list of similarity scores and their ids.
   */
  public SimilarityScoreManager() {
    similarityScoreList = new ArrayList<>();
    currentScoreID++; // Make the current score 0
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
   *
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

  /**
   * Gets the FileIndexPairs for a given score ID.
   *
   * @param scoreID The ID of the score to retrieve the FileIndexPair objects from.
   *
   * @return A List of FileIndexPair objects. This list should be of length 2.
   */
  public List<FileIndexPair> getSimilarFileIndexPairs(int scoreID) {
    List<FileIndexPair> indexPairList = new ArrayList<>();
    indexPairList.add(similarityScoreList.get(scoreID).fileIndexPair1);
    indexPairList.add(similarityScoreList.get(scoreID).fileIndexPair2);
    return indexPairList;
  }

  /**
   * Returns the contents of the similarityScoreList.
   *
   * @return The List, similarityScoreList.
   */
  public List<SimilarityScore> getSimilarityScoreList() {
    return similarityScoreList;
  }

  /**
   * Filters scores based off of a given filter code and a value.
   *
   * @param filter      A String containing "gt" (greater than), "lt" (less than), "ge" (greater equals), "le" (less
   *                    equals), "eq" (equals).
   * @param filterValue The value to filter the results with.
   *
   * @return A list of SimilarityScores matching the given filter, unless a bad filter was given in which case null will
   * be returned.
   */
  public List<SimilarityScore> filterScoresByNum(String filter, double filterValue) {
    List<SimilarityScore> resultsList = new ArrayList<>(); // Stores the scores matching the filter
    // Check if each score matches the given filter
    for (SimilarityScore score : similarityScoreList) {
      switch (filter) {
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

  /**
   * Filters scores based off of a given filter code and a value. This method is called when you want to filter results
   * from a list that has already been filtered, such as to filter both for and while loops, for example.
   *
   * @param filter     A String containing "eq" (equals) or "ne" (not equals) used to filter results.
   * @param filterType The FileIndexType that must be either matched or not matched by the given filter.
   * @param inputList  The input list of scores to filter.
   *
   * @return A list of SimilarityScores matching the given filter, unless a bad filter was given in which case null will
   * be returned.
   */
  public List<SimilarityScore> filterScoresByType(String filter, FileIndexType filterType,
                                                  List<SimilarityScore> inputList) {
    List<SimilarityScore> resultsList = new ArrayList<>(); // Stores the scores matching the filter
    // Check if each score matches the given filter
    for (SimilarityScore score : inputList) {
      switch (filter) {
        case "eq":
          if (score.fileIndexPair1.fileIndexType == filterType || score.fileIndexPair2.fileIndexType == filterType)
            resultsList.add(score);
          break;
        case "ne":
          if (!(score.fileIndexPair1.fileIndexType == filterType && score.fileIndexPair2.fileIndexType == filterType))
            resultsList.add(score);
          break;
        default:
          return null; // Return null if there was an incorrect filter supplied
      }
    }
    return resultsList; // Return the filtered list of scores
  }

  /**
   * Filters scores based off of a given filter code and a value.
   *
   * @param filter     A String containing "eq" (equals) or "ne" (not equals) used to filter results.
   * @param filterType The FileIndexType that must be either matched or not matched by the given filter.
   *
   * @return A list of SimilarityScores matching the given filter, unless a bad filter was given in which case null will
   * be returned.
   */
  public List<SimilarityScore> filterScoresByType(String filter, FileIndexType filterType) {
    // Filter scores from the main similarityScoreList
    return filterScoresByType(filter, filterType, similarityScoreList);
  }

  /**
   * Sorts the scores in descending order and reassigns the score IDs.
   */
  public void sortSimilarityScoreList() {
    // Sort the scores
    similarityScoreList.sort(new Comparator<SimilarityScore>() {
      @Override
      public int compare(SimilarityScore score1, SimilarityScore score2) {
        return ((int) (score2.similarityScore * 1000.0f) - (int) (score1.similarityScore * 1000.0f));
      }
    });
    System.out.println();
    // Replace the score IDs
    for (int i = currentScoreID - similarityScoreList.size(); i < currentScoreID; i++) {
      similarityScoreList.get(i).scoreId = i;
    }
  }
}
