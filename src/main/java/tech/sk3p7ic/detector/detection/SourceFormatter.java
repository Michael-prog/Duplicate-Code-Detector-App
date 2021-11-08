package tech.sk3p7ic.detector.detection;

import tech.sk3p7ic.detector.files.FileIndexPair;
import tech.sk3p7ic.detector.files.FileIndexType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceFormatter {
  private char varLetter; // Used to replace variable names
  private short varNumber; // Placed after varLetter to extend number of supported variables

  /**
   * Reformats a given source file to make it easier to generate scores from.
   */
  public SourceFormatter() {
    varLetter = 'A';
    varNumber = 0;
  }

  /**
   * Converts a given variable name into a new variable name that will be easier to do string comparisons with.
   *
   * @return The new variable name for a given variable.
   */
  private String getVarName() {
    if (varLetter > 'Z') { // Check if we need to reset the varLetter
      varLetter = 'A';
      varNumber++;
    }
    String varName = "__" + varLetter + varNumber + "__"; // Make a string like "__A0__"
    varLetter++;
    return varName;
  }

  /**
   * Resets the values for the varLetter and varNumber.
   */
  private void resetVarLetter() {
    varLetter = 'A';
    varNumber = 0;
  }

  /**
   * Reformats code stored in a FileIndexPair.
   *
   * @param fileIndexPair The FileIndexPair containing the code that will be reformatted in-place.
   */
  public void formatSourceInput(FileIndexPair fileIndexPair) {
    Map<Integer, String> input = fileIndexPair.content;
    // Create variable to store variables used in the source code and their new values
    Map<String, String> variableMap = new HashMap<>(); // <original_var, new_var_from_getVarName>
    for (int i = fileIndexPair.indexStart; i < fileIndexPair.indexEnd; i++) { // Iterate through the lines
      String line = removeInlineComments(input.get(i)); // Get the line that we will analyze without inline comments
      // Don't operate on the first line of a method
      if (i == fileIndexPair.indexStart && fileIndexPair.fileIndexType == FileIndexType.TYPE_METHOD) continue;
      input.replace(i, findVarAndReplace(line, variableMap)); // Replace the old line with the new one
    }
    resetVarLetter(); // Reset when we're done for the next block that we reformat
  }

  /**
   * Reformats a given list of FileIndexPair objects.
   *
   * @param input List containing the FileIndexPair objects that will be reformatted.
   */
  public void formatSourceInputList(List<FileIndexPair> input) {
    for (int i = 0; i < input.size(); i++) {
      formatSourceInput(input.get(i));
      input.set(i, input.get(i));
    }
  }

  /**
   * Finds a variable in a line and replaces it with a new variable.
   *
   * @param line        The line to be evaluated.
   * @param variableMap A map containing the old variable names from the source file and the new variable names that
   *                    have been assigned.
   *
   * @return The line, reformatted with the variable name replacements.
   */
  public String findVarAndReplace(String line, Map<String, String> variableMap) {
    // Check if there's a variable definition in a line and get that line
    StringBuilder stringBuilder = new StringBuilder(findVarDefAndReplace(line, variableMap));
    for (Map.Entry<String, String> entry : variableMap.entrySet()) { // Check for previous variables in line
      String newLine = stringBuilder.toString(); // The new line, after replacing variable definitions
      String regex = entry.getKey() + "[^\\w]"; // The regex used to detect if there's a variable in a line
      // Compile and match the regex
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(newLine);
      // If there's a match
      if (matcher.find()) {
        String match = matcher.group(0); // Get the match from the pattern
        // Use substring to get rid of non-alphanumeric character found by dblCheckRegex
        stringBuilder.replace(0, stringBuilder.length(), newLine.replaceAll(match.substring(0, match.length() - 1),
            entry.getValue()));
      }
    }
    return stringBuilder.toString(); // Return the new line
  }

  /**
   * Finds a variable definition in a line and replaces it with a new variable.
   *
   * @param line        The line to be evaluated.
   * @param variableMap A Map containing the old variable names from the source file and the new variable names that
   *                    have been assigned. If a variable definition is found, that variable and its new variable name
   *                    will be added to this Map.
   *
   * @return The line, with any variable name changes applied.
   */
  public String findVarDefAndReplace(String line, Map<String, String> variableMap) {
    String regex = "([a-zA-Z]+ \\w+;)|([a-zA-Z]+ \\w+.*=)|(> [a-zA-Z]+)"; // Used to detect parenthesis
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(line);
    boolean varInLine = matcher.find();
    if (!varInLine) return line; // There were no matches
    String currentVarName = matcher.group(0).strip().split(" ")[1]; // Get the name of the variable
    if (!variableMap.containsKey(currentVarName)) // If the current variable has already been defined
      variableMap.put(currentVarName, getVarName()); // Add the current variable name to the map
    String dblCheckRegex = currentVarName + "[^ \\w]"; // Used to double check that the chars we replace are correct
    pattern = Pattern.compile(dblCheckRegex);
    matcher = pattern.matcher(line);
    if (matcher.find()) {
      String checkedVar = matcher.group(0); // Get the variable that's being double-checked
      // Use substring to get rid of non-alphanumeric character found by dblCheckRegex
      return line.replaceAll(checkedVar.substring(0, checkedVar.length() - 1), variableMap.get(currentVarName));
    } else {
      return line.replaceFirst(currentVarName, variableMap.get(currentVarName));
    }
  }

  /**
   * Removes inline comments from a given line to make string comparisons easier.
   *
   * @param line The line to remove comments from.
   *
   * @return The line, with inline comments removed.
   */
  public String removeInlineComments(String line) {
    // TODO: Allow for "//" in strings to not be counted as a comment
    String commentRegex = "//.*$";
    return line.replaceAll(commentRegex, "");
  }
}
