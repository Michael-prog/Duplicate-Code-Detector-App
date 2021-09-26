package tech.sk3p7ic.detector.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReader {
  private File sourceFile; // The file that will be read

  private FileReader() {} // Get rid of default no-arg constructor // TODO: Detect one-liner functions

  /**
   * Create a new FileReader object to read and interpret the contents of a .java file.
   * @param sourceFile The .java file that will be read.
   */
  public FileReader(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  /**
   * Get the main class from the provided file.
   * @return A map containing the indexes of each line in the class and their contents on each line.
   * @throws IOException If the input source file cannot be read.
   */
  public Map<Integer, String> getClassFromFile() throws IOException {
    List<String> fileLines = Files.readAllLines(sourceFile.toPath()); // Get the contents of the source file
    Map<Integer, String> classLines = new LinkedHashMap<>(); // Stores the line indexes and lines of the source file
    Stack<Character> bracesStack = new Stack<>(); // Used to keep track of the braces
    boolean classDefinitionLineFound = false; // Flag to determine when the class definition has been found
    int lineIndex = 0; // Used to store the index of the current line
    while (!classDefinitionLineFound) {
      String line = fileLines.get(lineIndex); // Get the current line
      // Check if the line contains the classname and an opening brace on either same or next line
      if (line.contains(getFileName()) && line.contains("class") &&
              (line.contains("{") || fileLines.get(lineIndex + 1).contains("{"))) { // TODO: Figure out how to nt break the program with this line
        classDefinitionLineFound = true;
        classLines.put(lineIndex, line);
        bracesStack.push('{');
        if (!line.contains("{")) {
          lineIndex++; // Start from the next line index
          classLines.put(lineIndex, fileLines.get(lineIndex));
        }
      }
      lineIndex++;
    }
    while (!bracesStack.empty()) { // While the closing brace of the class definition has not been popped
      String line = fileLines.get(lineIndex); // Get the current line
      classLines.put(lineIndex, line);
      if (line.contains("{") && !isBraceInString(line, false)) {
        bracesStack.push('{');
        System.out.println(line + "\n\tPUSH\t" + bracesStack.size());
      }
      if (line.contains("}") && !isBraceInString(line, true)) {
        bracesStack.pop();
        System.out.println(line + "\n\tPOP\t" + bracesStack.size());
      }
      lineIndex++;
    }
    return classLines;
  }

  /**
   * Gets the methods contained within a given class.
   * @param classLines A map of line indexes and line strings that will be searched for methods.
   * @return A List of Maps of line indexes and their respective lines for each detected method.
   */
  public List<Map<Integer, String>> getMethodsFromFile(Map<Integer, String> classLines) {
    List<Map<Integer, String>> methodList = new ArrayList<>(); // Stores the maps of the methods in the class
    for (Map.Entry<Integer, String> lineEntry : classLines.entrySet()) {
      String line = lineEntry.getValue();
      if ((line.contains("public") || line.contains("private") || line.contains("protected")) &&
              line.contains("(") && !line.contains(" class ") && verifyMethodSignature(line)) { // Should indicate a method definition
        Map<Integer, String> methodLines = new LinkedHashMap<>(); // Stores the lines of the method
        int lineIndex = lineEntry.getKey(); // Get the current line index
        methodLines.put(lineIndex, line); // Add the line to the method lines
        // TODO: Maybe replace with `while (!line.contains("{")) to get the start of the method
        // TODO: Require '{' to determine if code is a method definition
        if (!line.contains("{") && classLines.get(lineIndex + 1).contains("{")) {
          lineIndex++;
          methodLines.put(lineIndex, line);
        }
        lineIndex++;
        Stack<Character> bracesStack = new Stack<>();
        bracesStack.add('{'); // Add the starting brace to the stack
        while (!bracesStack.empty()) { // While the closing brace of the method has not been popped
          String methodLine = classLines.get(lineIndex); // Get the current line
          methodLines.put(lineIndex, methodLine);
          if (methodLine.contains("{") && !isBraceInString(methodLine, false)) bracesStack.push('{');
          if (methodLine.contains("}") && !isBraceInString(methodLine, true)) bracesStack.pop();
          lineIndex++;
        }
        methodList.add(methodLines);
      }
    }
    return methodList;
  }

  /**
   * Gets the methods from a file.
   * @return A List containing maps containing line indexes and strings of lines in the detected method.
   * @throws IOException If the source file cannot be opened or read.
   */
  public List<Map<Integer, String>> getMethodsFromFile() throws IOException {
    return getMethodsFromFile(getClassFromFile()); // Get the class lines and return the methods
  }

  /**
   * Used to get the filename without the extension.
   * @return The name of the source file without the extension.
   */
  private String getFileName() {
    String fileName = sourceFile.getName();
    return fileName.substring(0, fileName.lastIndexOf("."));
  }

  /**
   * Checks whether a curly brace is contained within a string.
   * @param line The line that you want to check.
   * @return Whether a curly brace is in a string, unless there's another outside the string for another block of code.
   */
  private boolean isBraceInString(final String line, final boolean checkingClosingBrace) {
    boolean isBraceInString; // Flag for if there's a curly brace in the line
    String openingRegex = "\".*\\{.*\""; // Regex for if there's an opening brace in a String / Character
    String closingRegex = "\".*}.*\""; // Regex for if there's a closing curly brace in a String / Character
    // Set up the patterns
    Pattern openingPattern = Pattern.compile(openingRegex);
    Pattern closingPattern = Pattern.compile(closingRegex);
    Matcher openingMatcher = openingPattern.matcher(line);
    Matcher closingMatcher = closingPattern.matcher(line);
    isBraceInString = openingMatcher.find(); // True if there's a match found
    if (!isBraceInString) isBraceInString = closingMatcher.find(); // If a match not already found, check for closings
    if (!isBraceInString) { // If there's not a match found, check for '{' and '}'
      openingRegex = "'.*\\{.*'";
      closingRegex = "'.*}.*'";
      openingPattern = Pattern.compile(openingRegex);
      closingPattern = Pattern.compile(closingRegex);
      openingMatcher = openingPattern.matcher(line);
      closingMatcher = closingPattern.matcher(line);
      isBraceInString = openingMatcher.find();
      if (!isBraceInString) isBraceInString = closingMatcher.find();
    }
    if (isBraceInString && !checkingClosingBrace) { // Check to make sure it's not a line that starts another block of code
      String justInCaseRegex = " \\{.*";
      Pattern pattern = Pattern.compile(justInCaseRegex);
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) isBraceInString = false;
    }
    return isBraceInString;
  }

  /**
   * Checks whether a given line is a method or not based off of if there is an '=' in the line.
   * @param line The line that you want to check.
   * @return Whether the line is a method signature or not.
   */
  private boolean verifyMethodSignature(final String line) {
    Pattern isVariablePattern = Pattern.compile("[a-zA-Z]+ = [a-zA-Z]+"); // Ex. logger = Logger
    Matcher isVariableMatcher = isVariablePattern.matcher(line);
    return !isVariableMatcher.find(); // Return true if there's no matches
  }
}
