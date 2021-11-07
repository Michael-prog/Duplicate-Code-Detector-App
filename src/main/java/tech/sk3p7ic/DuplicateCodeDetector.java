package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import tech.sk3p7ic.cli.CliOptions;
import tech.sk3p7ic.detector.Detector;
import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;
import tech.sk3p7ic.gui.GraphicsLoader;

import java.io.File;
import java.util.*;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);

  public static void main(String[] args) {
    CliOptions cliOptions = new CliOptions();
    new CommandLine(cliOptions).parseArgs(args);
    if (cliOptions.useGUI) {
      GraphicsLoader graphicsLoader = new GraphicsLoader();
    } else { // Start the terminal app
      // Check that file inputs have been given
      if (cliOptions.file1 == null) {
        if (cliOptions.file2 != null) {// If this was not null for some reason
          cliOptions.file1 = cliOptions.file2; // Set the first file to be the second file
        } else { // If both are null
          File[] files = getFilenames();
          cliOptions.file1 = files[0];
          cliOptions.file2 = files[1];
        }
      } else {
        if (cliOptions.file2 == null) // If there was no second file specified
          cliOptions.file2 = cliOptions.file1; // Set the second file to equal the second.
      }
      System.out.println("Performing detection on '" + cliOptions.file1.getAbsolutePath() + "'");
      Detector mainDetector = new Detector(cliOptions.file1, cliOptions.file1);
      SimilarityScoreManager scoreManager = mainDetector.generateSimilarityScores(); // Get the scores
      printData(scoreManager.getSimilarityScoreList());
      userMenu(scoreManager);
    }
  }

  /**
   * Gets the filenames / files to be scanned from the user.
   * @return An array with 2 file objects.
   */
  private static File[] getFilenames() {
    Scanner scanner = new Scanner(System.in); // Scanner to get user input
    int numFilesChoice = 0;
    boolean isValidChoice = false;
    while (!isValidChoice) {
      System.out.print("Enter number of files to scan (1 or 2): ");
      if (scanner.hasNextInt()) {
        numFilesChoice = scanner.nextInt();
        if (numFilesChoice == 1 || numFilesChoice == 2)
          isValidChoice = true;
        else
          System.out.println("[!] Invalid choice! Please enter either '1' or '2'.");
      } else {
        System.out.println("[!] Invalid choice! Please enter either '1' or '2'.");
      }
    }
    // Get the files
    File[] files = new File[2]; // There will always be two files stored in this array
    isValidChoice = false;
    while (!isValidChoice) {
      // There will always be at least one file, so get that file
      // The ternary operator is used to print "first" if the user is trying to scan 2 different files
      System.out.print("Please enter the complete file path for the " + ((numFilesChoice == 1) ? "" : "first") +
              "file you would like to scan: ");
      String firstFilePath = scanner.nextLine();
      // Check that the user entered something
      if (firstFilePath == null || firstFilePath.length() == 0) {
        System.out.println("[!] Please enter a file path.");
        continue;
      }
      // Check that the path provided is for a java file by checking what extension follows the last '.'
      if (!firstFilePath.split("\\.")[firstFilePath.split("\\.").length - 1].equals("java")) {
        System.out.println("[!] File must be a java file.");
        continue;
      }
      File file = new File(firstFilePath);
      // Check if the file exists
      if (file.exists()) {
        files[0] = file;
        if (numFilesChoice == 1) files[1] = file; // Set second file to be the same if user only wants to scan one file
        isValidChoice = true;
      } else {
        System.out.println("[!] File does not exist!");
      }
    }
    if (numFilesChoice == 2) {
      isValidChoice = false;
      while (!isValidChoice) {
        // There will always be at least one file, so get that file
        // The ternary operator is used to print "first" if the user is trying to scan 2 different files
        System.out.print("Please enter the complete file path for the second file you would like to scan: ");
        String firstFilePath = scanner.nextLine();
        // Check that the user entered something
        if (firstFilePath == null || firstFilePath.length() == 0) {
          System.out.println("[!] Please enter a file path.");
          continue;
        }
        // Check that the path provided is for a java file by checking what extension follows the last '.'
        if (!firstFilePath.split("\\.")[firstFilePath.split("\\.").length - 1].equals("java")) {
          System.out.println("[!] File must be a java file.");
          continue;
        }
        File file = new File(firstFilePath);
        // Check if the file exists
        if (file.exists()) {
          files[1] = file;
          isValidChoice = true;
        } else {
          System.out.println("[!] File does not exist!");
        }
      }
    }
    return files;
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
              System.out.print(((i == 0) ? score.sourceFile1.getName() : score.sourceFile2.getName()));
              System.out.println(((i == 0) ? "\t" + score.fileIndexPair1.fileIndexType.toString() :
                      "\t" + score.fileIndexPair2.fileIndexType.toString()));
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
        System.out.print(""); // Used to do nothing if the user does not want to do anything
      } else {
        System.out.println("Invalid input: '" + userInput + "'");
        printHelp();
      }
    } while (!(userInput.contains("quit") || userInput.contains("exit")));
  }

  public static void filterResults(String userInput, SimilarityScoreManager scoreManager) {
    try {
      String[] command = userInput.split(" ");
      List<SimilarityScore> resultsList = scoreManager.filterScores(command[1], Double.parseDouble(command[2]));
      if (!(resultsList == null))
        printData(resultsList);
      else
        printHelp();
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
    System.out.println("+-------+-------+-----------------+-----------------+----------------------" +
                       "+----------------------+");
    for (SimilarityScore score : scores) {
      System.out.printf("| %-5d | %-5.2f | %-15s | %-15s | %-20s | %-20s |%n", score.scoreId, score.similarityScore,
              score.sourceFile1.getName(), score.sourceFile2.getName(), score.fileIndexPair1.fileIndexType,
              score.fileIndexPair2.fileIndexType);
    }
  }
}
