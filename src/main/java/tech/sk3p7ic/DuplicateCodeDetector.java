package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import tech.sk3p7ic.cli.CliOptions;
import tech.sk3p7ic.detector.Detector;
import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;

import java.util.*;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);

  public static void main(String[] args) {
    CliOptions cliOptions = new CliOptions();
    new CommandLine(cliOptions).parseArgs(args);
    System.out.println("Performing detection on '" + cliOptions.file1.getAbsolutePath() + "'");
    Detector mainDetector = new Detector(cliOptions.file1, cliOptions.file1);
    SimilarityScoreManager scoreManager = mainDetector.generateSimilarityScores(); // Get the scores
    printData(scoreManager.getSimilarityScoreList());
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
            for (int i = 0; i < lines.size(); i++) {
              Map<Integer, String> list = lines.get(i);
              SimilarityScore score = scoreManager.getSimilarityScoreList().get(Integer.parseInt(command[1]));
              System.out.println(((i == 0) ? score.sourceFile1.getName() : score.sourceFile2.getName()));
              for (Map.Entry<Integer, String> entry : list.entrySet()) {
                System.out.println(entry.getKey() + "\t:" + entry.getValue());
              }
            }
          } catch (Exception e) {
            logger.error(e.getMessage());
            printHelp();
          }
        } else {
          System.out.println("Invalid arguments.\n");
          printHelp();
        }
      } else if (userInput.contains("filter")) {
        System.out.println("Filtering results.");
        filterResults(userInput, scoreManager);
      } else if (userInput.contains("quit") || userInput.contains("exit")) {
        System.out.println("Exiting...");
      } else if (userInput.equals("")) {
        continue; // Used to do nothing if the user does not want to do anything
      } else {
        System.out.println("Invalid input: '" + userInput + "'");
        printHelp();
      }
    } while (!(userInput.contains("quit") || userInput.contains("exit")));
  }

  public static void filterResults(String userInput, SimilarityScoreManager scoreManager) {
    try {
      String[] command = userInput.split(" ");
      List<SimilarityScore> resultsList = new ArrayList<>();
      if (command[1].equals("gt")) {
        for (SimilarityScore score : scoreManager.getSimilarityScoreList()) {
          if (score.similarityScore > Double.parseDouble(command[2])) resultsList.add(score);
        }
        printData(resultsList);
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

  public static void printData(List<SimilarityScore> scores) {
    // TODO: Base filename column widths off of length of filename
    System.out.printf("| %-5s | %-5s | %-15s | %-15s | %-20s | %-20s |%n", "ID", "Score", "File 1 Name", "File 2 Name",
            "File 1 Index Type", "File 2 Index Type");
    for (SimilarityScore score : scores) {
      System.out.printf("| %-5d | %-5.2f | %-15s | %-15s | %-20s | %-20s |%n", score.scoreId, score.similarityScore,
              score.sourceFile1.getName(), score.sourceFile2.getName(), score.fileIndexPair1.fileIndexType,
              score.fileIndexPair2.fileIndexType);
    }
  }
}
