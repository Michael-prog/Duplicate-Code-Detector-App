package tech.sk3p7ic.detector.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SourceFile {
  private static Logger logger = LoggerFactory.getLogger(SourceFile.class);
  private Map<Integer, String> fileClass;
  private List<FileIndexPair> fileIndexPairs;
  private File sourceFile;
  private FileReader reader;

  private SourceFile() {
  } // Prevent instantiation with no-arg constructor

  public SourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
    fileIndexPairs = new ArrayList<>();
    reader = new FileReader(sourceFile);
    fileClass = null;
  }

  public SourceFile(String filePath) {
    this(new File(filePath));
  }

  public void generateClassFromFile() {
    try {
      fileClass = reader.getClassFromFile();
      Object[] lineIndexSet = fileClass.keySet().toArray(); // Used to get first and last line numbers
      fileIndexPairs.add(new FileIndexPair(FileIndexType.TYPE_CLASS, (int) lineIndexSet[0],
              (int) lineIndexSet[fileClass.size() - 1], fileClass));
    } catch (IOException e) {
      logger.error(e.getMessage()); // Log that there was an error
    }
  }

  public void generateMethodsFromFile() {
    List<Map<Integer, String>> methodList = reader.getMethodsFromFile(fileClass);
    for (Map<Integer, String> method : methodList) {
      Object[] lineIndexSet = method.keySet().toArray();
      fileIndexPairs.add(new FileIndexPair(FileIndexType.TYPE_METHOD, (int) lineIndexSet[0],
              (int) lineIndexSet[method.size() - 1], method));
    }
  }

  public void generateForLoopsFromMethod() {
    for (FileIndexPair fileIndexPair : fileIndexPairs) {
      if (fileIndexPair.fileIndexType == FileIndexType.TYPE_METHOD) {
        List<Map<Integer, String>> forLoopsList = reader.getForLoopsFromMethod(fileIndexPair.content);
        for (Map<Integer, String> forLoop : forLoopsList) {
          Object[] lineIndexSet = forLoop.keySet().toArray();
          fileIndexPairs.add(new FileIndexPair(FileIndexType.TYPE_FOR_LOOP, (int) lineIndexSet[0],
                  (int) lineIndexSet[forLoop.size() - 1], forLoop));
        }
      }
    }
  }

  public List<FileIndexPair> getFileIndexPairs() {
    return fileIndexPairs;
  }
}