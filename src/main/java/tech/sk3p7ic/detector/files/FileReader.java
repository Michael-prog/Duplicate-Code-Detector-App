package tech.sk3p7ic.detector.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReader {
  private File sourceFile; // The file that will be read

  private FileReader() {
  } // Get rid of default no-arg constructor

  /**
   * Create a new FileReader object to read and interpret the contents of a .java file.
   *
   * @param sourceFile The .java file that will be read.
   */
  public FileReader(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  /**
   * Get the main class from the provided file.
   *
   * @return A map containing the indexes of each line in the class and their contents on each line.
   *
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
      if (line.contains(getFileName()) && line.contains("class")) {
        classDefinitionLineFound = true;
        classLines.put(lineIndex, line);
        while (!fileLines.get(lineIndex).contains("{")) {
          lineIndex++;
          classLines.put(lineIndex, fileLines.get(lineIndex));
        }
        if (fileLines.get(lineIndex).contains("}")) { // The end of function is on the same line as the start
          return classLines;
        }
        bracesStack.push('{');
      }
      lineIndex++;
    }
    while (!bracesStack.empty()) { // While the closing brace of the class definition has not been popped
      String line = fileLines.get(lineIndex); // Get the current line
      classLines.put(lineIndex, line);
      if (line.contains("{") && braceIsValid(line, false)) bracesStack.push('{');
      if (line.contains("}") && braceIsValid(line, true)) bracesStack.pop();
      lineIndex++;
    }
    return classLines;
  }

  /**
   * Gets the methods contained within a given class.
   *
   * @param classLines A map of line indexes and line strings that will be searched for methods.
   *
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
        while (!classLines.get(lineIndex).contains("{")) {
          lineIndex++;
          methodLines.put(lineIndex, classLines.get(lineIndex));
        }
        if (methodLines.get(lineIndex).contains("}")) {
          methodList.add(methodLines);
          continue;
        }
        lineIndex++;
        Stack<Character> bracesStack = new Stack<>();
        bracesStack.add('{'); // Add the starting brace to the stack
        while (!bracesStack.empty()) { // While the closing brace of the method has not been popped
          String methodLine = classLines.get(lineIndex); // Get the current line
          methodLines.put(lineIndex, methodLine);
          if (methodLine.contains("{") && braceIsValid(methodLine, false)) bracesStack.push('{');
          if (methodLine.contains("}") && braceIsValid(methodLine, true)) bracesStack.pop();
          lineIndex++;
        }
        methodList.add(methodLines);
      }
    }
    return methodList;
  }

  /**
   * Gets the methods from a file.
   *
   * @return A List containing maps containing line indexes and strings of lines in the detected method.
   *
   * @throws IOException If the source file cannot be opened or read.
   */
  public List<Map<Integer, String>> getMethodsFromFile() throws IOException {
    return getMethodsFromFile(getClassFromFile()); // Get the class lines and return the methods
  }

  /**
   * Gets the for or while loops from a method, depending on the FileIndexType passed to the function.
   *
   * @param contentLines The lines for the given content.
   * @param type         The FileIndexType to detect. Assumes that if given type is not 'TYPE_FOR_LOOP', then 'TYPE_WHILE_LOOP'
   *                     is meant.
   *
   * @return A list containing maps of the line numbers and lines contained in each loop.
   */
  public List<Map<Integer, String>> getLoopsFromContent(Map<Integer, String> contentLines, FileIndexType type) {
    List<Map<Integer, String>> loopList = new ArrayList<>();
    for (Map.Entry<Integer, String> lineEntry : contentLines.entrySet()) {
      String line = lineEntry.getValue();
      String detectionString = (type == FileIndexType.TYPE_FOR_LOOP) ? "for (" : "while (";
      if (line.contains(detectionString)) { // If line contains the start of a for / while loop
        Map<Integer, String> loopLines = new LinkedHashMap<>();
        int lineIndex = lineEntry.getKey(); // Get the starting line index
        loopLines.put(lineIndex, line); // Add the line to the map
        while (!contentLines.get(lineIndex).contains("{")) { // Loop until the opening brace is in the line
          lineIndex++;
          loopLines.put(lineIndex, contentLines.get(lineIndex));
        }
        if (loopLines.get(lineIndex).contains("}")) {
          loopList.add(loopLines);
          continue;
        }
        lineIndex++;
        Stack<Character> bracesStack = new Stack<>();
        bracesStack.add('{');
        while (!bracesStack.empty()) {
          String loopLine = contentLines.get(lineIndex);
          loopLines.put(lineIndex, loopLine);
          if (loopLine.contains("{") && braceIsValid(loopLine, false)) bracesStack.push('{');
          if (loopLine.contains("}") && braceIsValid(loopLine, true)) bracesStack.pop();
          lineIndex++;
        }
        loopList.add(loopLines);
      }
    }
    return loopList;
  }

  /**
   * Used to get the filename without the extension.
   *
   * @return The name of the source file without the extension.
   */
  private String getFileName() {
    String fileName = sourceFile.getName();
    return fileName.substring(0, fileName.lastIndexOf("."));
  }

  /**
   * Checks whether a curly brace is contained within a string.
   *
   * @param line The line that you want to check.
   *
   * @return Whether a curly brace is in a string, unless there's another outside the string for another block of code.
   */
  private boolean braceIsValid(final String line, final boolean checkingClosingBrace) {
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
    return !isBraceInString;
  }

  /**
   * Checks whether a given line is a method or not based off of if there is an '=' in the line.
   *
   * @param line The line that you want to check.
   *
   * @return Whether the line is a method signature or not.
   */
  private boolean verifyMethodSignature(final String line) {
    Pattern isVariablePattern = Pattern.compile("[a-zA-Z]+ = [a-zA-Z]+"); // Ex. logger = Logger
    Matcher isVariableMatcher = isVariablePattern.matcher(line);
    return !isVariableMatcher.find(); // Return true if there's no matches
  }
}
