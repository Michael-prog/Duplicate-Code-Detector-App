package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import tech.sk3p7ic.cli.CliOptions;
import tech.sk3p7ic.detector.Detector;
import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;
import tech.sk3p7ic.detector.files.FileIndexType;
import tech.sk3p7ic.gui.GraphicsLoader;

import java.io.File;
import java.util.*;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);

  /**
   * Starts the Duplicate Code Detector App.
   * @param args Command-line arguments that will be parsed. See documentation for a list of arguments.
   */
  public static void main(String[] args) {
    // Parse the command line arguments
    CliOptions cliOptions = new CliOptions();
    new CommandLine(cliOptions).parseArgs(args);
    if (cliOptions.useGUI) { // Start the GUI
      GraphicsLoader.start();
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
      // Start the detection and get the scores
      Detector mainDetector = new Detector(cliOptions.file1, cliOptions.file1);
      SimilarityScoreManager scoreManager = mainDetector.generateSimilarityScores(); // Get the scores
      scoreManager.sortSimilarityScoreList(); // Sort the scores in descending order
      printData(scoreManager.getSimilarityScoreList()); // Display the scores to the user
      userMenu(scoreManager); // Start the menu so the user can interact with the scores
    }
  }

  /**
   * Gets the filenames / files to be scanned from the user.
   * @return An array with 2 file objects.
   */
  private static File[] getFilenames() {
    Scanner scanner = new Scanner(System.in); // Scanner to get user input
    int numFilesChoice = 0;
    while (!(numFilesChoice == 1 || numFilesChoice == 2)) {
      System.out.print("Enter number of files to scan (1 or 2): ");
      // Attempt to get the number from the user
      try {
        String userInput = scanner.nextLine(); // First get a string of the text that the user enters
        numFilesChoice = Integer.parseInt(userInput.strip()); // Attempt to parse the user input into an int
        // Check if the user did not enter 1 or 2
        if (!(numFilesChoice == 1 || numFilesChoice == 2)) {
          System.out.println("[!] Please enter a valid number (1 or 2).");
        }
      } catch (NumberFormatException e) { // if there was an error parsing the integer
        System.out.println("[!] Please enter a valid number (1 or 2).");
      }
    }
    // Get the files
    File[] files = new File[2]; // There will always be two files stored in this array
    boolean isValidChoice = false;
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

  /**
   * Runs a menu that the user can interact with to further investigate duplicate blocks of code.
   * @param scoreManager The SimilarityScoreManager object that's storing the similarity scores that were generated on
   *                     the blocks of code from the input source file(s).
   */
  public static void userMenu(SimilarityScoreManager scoreManager) {
    Scanner scanner = new Scanner(System.in); // Scanner to get user input
    String userInput; // Stores the input from the user
    do {
      System.out.print("$ DCD >> "); // The prompt
      userInput = scanner.nextLine(); // Get what the user enters
      userInput = userInput.toLowerCase(Locale.ROOT); // Force characters into lowercase for easier comparison
      if (userInput.contains("help")) { // Help -- Show the commands
        printHelp();
      } else if (userInput.contains("dig")) { // Dig -- Look at the code for similar blocks of code
        String[] command = userInput.split(" ");
        if (command.length == 2) {
          try {
            // Get the lines from the source file(s) and display
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
      } else if (userInput.contains("filter")) { // Filter -- Filter results with a given filter and value
        System.out.println("Filtering results.");
        filterResults(userInput, scoreManager);
      } else if (userInput.contains("quit") || userInput.contains("exit")) { // Exit the program
        System.out.println("Exiting...");
      } else if (userInput.equals("")) {
        System.out.print(""); // Used to do nothing if the user does not want to do anything
      } else {
        System.out.println("Invalid input: '" + userInput + "'");
        printHelp();
      }
    } while (!(userInput.contains("quit") || userInput.contains("exit")));
  }

  /**
   * Filters results with a given input and SimilarityScoreManager.
   * @param userInput The input string that the user entered. In a perfect case, will be of the form "filter gt 0.69",
   *                  for example.
   * @param scoreManager SimilarityScoreManager containing the similarity scores and their IDs.
   */
  public static void filterResults(String userInput, SimilarityScoreManager scoreManager) {
    try {
      // Split the command and attempt to get the results of the filter
      String[] command = userInput.split(" ");
      String filter = command[1];
      try {
        double filterValue = Double.parseDouble(command[2]); // Attempt to parse second value to double
        List<SimilarityScore> resultsList = scoreManager.filterScoresByNum(filter, filterValue);
        if (!(resultsList == null)) // Print the results
          printData(resultsList);
        else // If the user gave an invalid filter
          printHelp();
      } catch (NumberFormatException e) { // If there was an error parsing the input to double
        String filterValueString = command[2]; // The value that the user wants to filter by
        // Turn the filterValueString into a FileIndexType and filter
        FileIndexType filterValueType;
        List<SimilarityScore> resultsList;
        switch (filterValueString) {
          case "class": case "type_class":
            filterValueType = FileIndexType.TYPE_CLASS;
            resultsList = scoreManager.filterScoresByType(filter, filterValueType);
            break;
          case "method": case "type_method":
            filterValueType = FileIndexType.TYPE_METHOD;
            resultsList = scoreManager.filterScoresByType(filter, filterValueType);
            break;
          case "loop":
            if (filter.equals("ne")) {
              List<SimilarityScore> tempResultsList; // Temp list to store results in case of multiple filters
              // Get the for loops
              filterValueType = FileIndexType.TYPE_FOR_LOOP;
              tempResultsList = scoreManager.filterScoresByType(filter, filterValueType);
              // Get the while loops
              filterValueType = FileIndexType.TYPE_WHILE_LOOP;
              tempResultsList = scoreManager.filterScoresByType(filter, filterValueType, tempResultsList);
              // Get the do-while loops
              filterValueType = FileIndexType.TYPE_DO_WHILE_LOOP;
              tempResultsList = scoreManager.filterScoresByType(filter, filterValueType, tempResultsList);
              resultsList = tempResultsList;
            } else { // Filter is "eq"
              // Get the for loops
              filterValueType = FileIndexType.TYPE_FOR_LOOP;
              resultsList = scoreManager.filterScoresByType(filter, filterValueType);
              // Get the while loops
              filterValueType = FileIndexType.TYPE_WHILE_LOOP;
              resultsList.addAll(scoreManager.filterScoresByType(filter, filterValueType));
              // Get the do-while loops
              filterValueType = FileIndexType.TYPE_DO_WHILE_LOOP;
              resultsList.addAll(scoreManager.filterScoresByType(filter, filterValueType));
            }
            break;
          case "for": case "type_for_loop":
            filterValueType = FileIndexType.TYPE_FOR_LOOP;
            resultsList = scoreManager.filterScoresByType(filter, filterValueType);
            break;
          case "while": case "type_while_loop":
            filterValueType = FileIndexType.TYPE_WHILE_LOOP;
            resultsList = scoreManager.filterScoresByType(filter, filterValueType);
            break;
          case "do-while": case "type_do_while":
            filterValueType = FileIndexType.TYPE_DO_WHILE_LOOP;
            resultsList = scoreManager.filterScoresByType(filter, filterValueType);
            break;
          case "switch": case "type_switch":
            filterValueType = FileIndexType.TYPE_SWITCH;
            resultsList = scoreManager.filterScoresByType(filter, filterValueType);
            break;
          default:
            printHelp();
            return; // Return null to end the method call
        }
        if (!(resultsList == null)) // Print the results
          printData(resultsList);
        else // If the user gave an invalid filter
          printHelp();
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  /**
   * Prints the help prompt containing the commands that the user may enter into the menu.
   */
  public static void printHelp() {
    System.out.println("Duplicate Code Detector -- Help");
    System.out.println("filter [gt,lt,ge,le,eq] <value> : Filter the output by a given value that is");
    System.out.println("                                  either greater than, less than, greater equal,");
    System.out.println("                                  less equal, or equal to the given value.");
    System.out.println("dig <scoreId>                   : \"Dig\" into a similarity score to view");
    System.out.println("                                  similar blocks of code.");
    System.out.println("exit                            : Exit the program.");
  }

  /**
   * Prints the input data in a nice table format.
   * @param scores List of SimilarityScore objects containing the information that will be printed.
   */
  public static void printData(List<SimilarityScore> scores) {
    // TODO: Base filename column widths off of length of filename
    // Print the column headers
    System.out.printf("| %-5s | %-5s | %-15s | %-15s | %-20s | %-20s |%n", "ID", "Score", "File 1 Name", "File 2 Name",
            "File 1 Index Type", "File 2 Index Type");
    System.out.println("+-------+-------+-----------------+-----------------+----------------------" +
                       "+----------------------+");
    // Print the scores and their other information
    for (SimilarityScore score : scores) {
      System.out.printf("| %-5d | %-5.2f | %-15s | %-15s | %-20s | %-20s |%n", score.scoreId, score.similarityScore,
              score.sourceFile1.getName(), score.sourceFile2.getName(), score.fileIndexPair1.fileIndexType,
              score.fileIndexPair2.fileIndexType);
    }
  }
}
