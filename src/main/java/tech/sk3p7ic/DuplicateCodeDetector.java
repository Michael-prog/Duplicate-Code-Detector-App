package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import tech.sk3p7ic.cli.CliOptions;
import tech.sk3p7ic.detector.Detector;
import tech.sk3p7ic.detector.detection.SimilarityScore;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);

  public static void main(String[] args) {
    CliOptions cliOptions = new CliOptions();
    new CommandLine(cliOptions).parseArgs(args);
    System.out.println("Performing detection on '" + cliOptions.file1.getAbsolutePath() + "'");
    Detector mainDetector = new Detector(cliOptions.file1, cliOptions.file1);
    List<SimilarityScore> scores = mainDetector.generateSimilarityScores(); // Get the scores
    for (SimilarityScore score : scores) {
      System.out.println("| " + score.scoreId + "\t| " + score.similarityScore + "\t|");
    }
    userMenu(scores);
  }

  public static void userMenu(List<SimilarityScore> similarityScores) {
    Scanner scanner = new Scanner(System.in); // Scanner to get user input
    String userInput;
    do {
      System.out.print("$ DCD >> ");
      userInput = scanner.nextLine();
      userInput = userInput.toLowerCase(Locale.ROOT); // Force characters into lowercase for easier comparison
      if (userInput.contains("help")) {
        printHelp();
      } else if (userInput.contains("dig")) {
        System.out.println("Doing something"); // Eventually do something
      } else if (userInput.contains("quit") || userInput.contains("exit")) {
        continue; // Used to prevent accidentally printing the help message when the user is trying to exit
      } else {
        System.out.println("Invalid input: '" + userInput + "'");
        printHelp();
      }
    } while (!(userInput.contains("quit") || userInput.contains("exit")));
  }

  public static void printHelp() {
    System.out.println("Duplicate Code Detector -- Help");
    System.out.println("dig <scoreId> : \"Dig\" into a similarity score to view similar blocks of code.");
    System.out.println("exit          : Exit the program.");
  }
}
