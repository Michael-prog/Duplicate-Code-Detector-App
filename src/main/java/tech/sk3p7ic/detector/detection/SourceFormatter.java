package tech.sk3p7ic.detector.detection;

import tech.sk3p7ic.detector.files.FileIndexPair;
import tech.sk3p7ic.detector.files.FileIndexType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceFormatter {
  char varLetter; // Used to replace variable names
  short varNumber; // Placed after varLetter to extend number of supported variables

  public SourceFormatter() {
    varLetter = 'A';
    varNumber = 0;
  }

  private String getVarName() {
    if (varLetter > 'Z') {
      varLetter = 'A';
      varNumber++;
    }
    String varName = "__" + varLetter + varNumber + "__";
    varLetter++;
    return varName;
  }

  private void resetVarLetter() {
    varLetter = 'A';
    varNumber = 0;
  }

  public void formatSourceInput(FileIndexPair fileIndexPair) {
    Map<Integer, String> input = fileIndexPair.content;
    // Create variable to store variables used in the source code and their new values
    Map<String, String> variableMap = new HashMap<>(); // <original_var, new_var_from_gerVarName>
    for (int i = fileIndexPair.indexStart; i < fileIndexPair.indexEnd; i++) { // Iterate through the lines
      String line = removeInlineComments(input.get(i)); // Get the line that we will analyze without inline comments
      // Don't operate on the first line of a method
      if (i == fileIndexPair.indexStart && fileIndexPair.fileIndexType == FileIndexType.TYPE_METHOD) continue;
      input.replace(i, findVarAndReplace(line, variableMap)); // Replace the old line with the new one
    }
    resetVarLetter();
  }

  public void formatSourceInputList(List<FileIndexPair> input) {
    for (int i = 0; i < input.size(); i++) {
      formatSourceInput(input.get(i));
      input.set(i, input.get(i));
    }
  }

  public String findVarAndReplace(String line, Map<String, String> variableMap) {
    StringBuilder stringBuilder = new StringBuilder(findVarDefAndReplace(line, variableMap));
    for (Map.Entry<String, String> entry : variableMap.entrySet()) { // Check for previous variables in line
      String newLine = stringBuilder.toString();
      stringBuilder.replace(0, stringBuilder.length(), newLine.replaceAll(entry.getKey(), entry.getValue()));
    }
    return stringBuilder.toString();
  }

  public String findVarDefAndReplace(String line, Map<String, String> variableMap) {
    String regex = "([a-zA-Z]+ [a-zA-z0-9]+;)|([a-zA-Z]+ [a-zA-z0-9]+.*=)|(> [a-zA-Z]+)"; // Used to detect parenthesis
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(line);
    boolean varInLine = matcher.find();
    if (!varInLine) return line; // There were no matches
    String currentVarName = matcher.group(0).strip().split(" ")[1]; // Get the name of the variable
    if (!variableMap.containsKey(currentVarName)) // If the current variable has already been defined
      variableMap.put(currentVarName, getVarName()); // Add the current variable name to the map
    return line.replaceAll(currentVarName, variableMap.get(currentVarName));
  }

  public String removeInlineComments(String line) {
    // TODO: Allow for "//" in strings to not be counted as a comment
    String commentRegex = "//.*$";
    return line.replaceAll(commentRegex, "");
  }
}
