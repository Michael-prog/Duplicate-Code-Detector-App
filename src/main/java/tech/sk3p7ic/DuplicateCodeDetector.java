package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import tech.sk3p7ic.cli.CliOptions;
import tech.sk3p7ic.detector.Detector;
import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;
import tech.sk3p7ic.detector.files.FileIndexPair;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);

  public static void main(String[] args) {
    CliOptions cliOptions = new CliOptions();
    new CommandLine(cliOptions).parseArgs(args);
    System.out.println("Performing detection on '" + cliOptions.file1.getAbsolutePath() + "'");
    Detector mainDetector = new Detector(cliOptions.file1, cliOptions.file1);
    SimilarityScoreManager scoreManager = mainDetector.generateSimilarityScores(); // Get the scores
    for (SimilarityScore score : scoreManager.getSimilarityScoreList()) {
      System.out.println("| " + score.scoreId + "\t| " + score.similarityScore + "\t|");
    }
    userMenu(scoreManager);
  }

  public static void userMenu(SimilarityScoreManager scoreManager) {
    Scanner scanner = new Scanner(System.in); // Scanner to get user input
    String userInput;
    do {
      System.out.print("$ DCD >> ");
      userInput = scanner.nextLine();
      userInput = userInput.toLowerCase(Locale.ROOT); // Force characters into lowercase for easier comparison
      if (userInput.contains("help")) {
        printHelp();
      } else if (userInput.contains("dig")) {
        String[] command = userInput.split(" ");
        if (command.length == 2) {
          try {
            List<Map<Integer, String>> lines = scoreManager.getSimilarityScoreLines(Integer.parseInt(command[1]));
            List<FileIndexPair> fileIndexPairs = scoreManager.getSimilarFileIndexPairs(Integer.parseInt(command[1]));
            for (Map<Integer, String> list : lines) {
              for (Map.Entry<Integer, String> entry : list.entrySet()) {
                System.out.println(entry.getKey() + "\t:" + entry.getValue());
              }
            }
          } catch (Exception e) {
            logger.error(e.getMessage());
          }
        }
      } else if (userInput.contains("filter")) {
        System.out.println("Filtering results.");
        filterResults(userInput, scoreManager);
      } else if (userInput.contains("quit") || userInput.contains("exit")) {
        System.out.println("Exiting...");
      } else {
        System.out.println("Invalid input: '" + userInput + "'");
        printHelp();
      }
    } while (!(userInput.contains("quit") || userInput.contains("exit")));
  }

  public static void filterResults(String userInput, SimilarityScoreManager scoreManager) {
    try {
      String[] command = userInput.split(" ");
      if (command[1].equals("gt")) {
        for (SimilarityScore score : scoreManager.getSimilarityScoreList()) {
          if (score.similarityScore > Double.parseDouble(command[2]))
            System.out.println("| " + score.scoreId + "\t| " + score.similarityScore + "\t|");
        }
      } else {
        printHelp();
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  public static void printHelp() {
    System.out.println("Duplicate Code Detector -- Help");
    System.out.println("filter [gt,lt,ge,le,eq] <value> : Filter the output by a given value that is");
    System.out.println("                                  either greater than, less than, greater equal,");
    System.out.println("                                  less equal, or equal to the given value.");
    System.out.println("dig <scoreId>                   : \"Dig\" into a similarity score to view");
    System.out.println("                                  similar blocks of code.");
    System.out.println("exit                            : Exit the program.");
  }
}
