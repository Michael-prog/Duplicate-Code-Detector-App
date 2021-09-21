package tech.sk3p7ic.detector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FileReader {
  private File sourceFile;

  private FileReader() {} // Get rid of default no-arg constructor

  public FileReader(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  public Map<Integer, String> getClassFromFile() throws IOException {
    List<String> fileLines = Files.readAllLines(sourceFile.toPath());
    Map<Integer, String> classLines = new LinkedHashMap<>();
    Stack<Character> bracesStack = new Stack<>(); // Used to keep track of the braces
    boolean classDefinitionLineFound = false;
    int lineIndex = 0;
    while (!classDefinitionLineFound) {
      String line = fileLines.get(lineIndex);
      if (line.contains(getFileName()) && line.contains("class") &&
              (line.contains("{") || fileLines.get(lineIndex + 1).contains("{"))) {
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

    while (!bracesStack.empty()) {
      String line = fileLines.get(lineIndex);
      classLines.put(lineIndex, line);
      if (line.contains("{")) bracesStack.push('{');
      if (line.contains("}")) bracesStack.pop();
      lineIndex++;
    }
    return classLines;
  }

  private String getFileName() {
    String fileName = sourceFile.getName();
    return fileName.substring(0, fileName.lastIndexOf("."));
  }
}
