package tech.sk3p7ic.detector.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sk3p7ic.detector.detection.SourceFormatter;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

  public File getSourceFile() {
    return sourceFile;
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

  /**
   * Gets the for and while loops from the source file.
   *
   * @param startingIndexSet A set used to ensure that the loops are not added twice.
   */
  public void generateLoopsFromMethod(Set<Integer> startingIndexSet) {
    // Get the current pairs list
    Object[] currentPairs = fileIndexPairs.toArray(); // Stored in this manner to avoid ConcurrentModificationException
    // Iterate through the pairs
    for (Object fileIndexPairObject : currentPairs) {
      FileIndexPair fileIndexPair = (FileIndexPair) fileIndexPairObject; // Cast Object so that we may work with it
      // If the pair is a method, run loop detection inside of it
      if (fileIndexPair.fileIndexType != FileIndexType.TYPE_CLASS) {
        // Detect for loops
        List<Map<Integer, String>> forLoopsList;
        try {
          forLoopsList = reader.getLoopsFromContent(fileIndexPair.content,
                  FileIndexType.TYPE_FOR_LOOP);
        } catch (IllegalArgumentException e) {
          logger.error(e.getMessage());
          continue;
        }
        for (Map<Integer, String> forLoop : forLoopsList) {
          Object[] forLineIndexSet = forLoop.keySet().toArray();
          if (startingIndexSet.contains(forLineIndexSet[0])) continue; // If the loop has already been added, skip it
          else startingIndexSet.add((int) forLineIndexSet[0]); // If the loop has not been added, add it to the set
          fileIndexPairs.add(new FileIndexPair(FileIndexType.TYPE_FOR_LOOP, (int) forLineIndexSet[0],
                  (int) forLineIndexSet[forLoop.size() - 1], forLoop));
        }
        // Detect while loops
        List<Map<Integer, String>> whileLoopsList;
        try {
          whileLoopsList = reader.getLoopsFromContent(fileIndexPair.content,
                  FileIndexType.TYPE_WHILE_LOOP);
        } catch (IllegalArgumentException e) {
          logger.error(e.getMessage());
          continue;
        }
        for (Map<Integer, String> whileLoop : whileLoopsList) {
          Object[] whileLineIndexSet = whileLoop.keySet().toArray();
          if (startingIndexSet.contains(whileLineIndexSet[0])) continue;
          else startingIndexSet.add((int) whileLineIndexSet[0]);
          fileIndexPairs.add(new FileIndexPair(FileIndexType.TYPE_WHILE_LOOP, (int) whileLineIndexSet[0],
                  (int) whileLineIndexSet[whileLoop.size() - 1], whileLoop));
        }
      }
    }
    // Retry generation until there's been no new data added
    if (fileIndexPairs.size() != currentPairs.length) generateLoopsFromMethod(startingIndexSet);
  }

  /**
   * Gets the for and while loops from the given source file.
   */
  public void generateLoopsFromMethod() {
    generateLoopsFromMethod(new HashSet<>());
  }

  public void testReplacements() {
    SourceFormatter formatter = new SourceFormatter();
    for (FileIndexPair pair : fileIndexPairs){
      if (pair.fileIndexType == FileIndexType.TYPE_CLASS) continue;
      formatter.formatSourceInput(pair);
      for (Map.Entry<Integer, String> entry : pair.content.entrySet()) {
        System.out.println(entry.getKey() + ":\t" + entry.getValue());
      }
      System.out.println("\n");
    }
  }

  /**
   * Generates all items, in order.
   */
  public void generateAll() {
    generateClassFromFile();
    generateMethodsFromFile();
    generateLoopsFromMethod();
    testReplacements();
  }

  /**
   * Gets the list of FileIndexPair objects that have been created.
   *
   * @return A List containing FileIndexPair objects created through reading the source file.
   */
  public List<FileIndexPair> getFileIndexPairs() {
    return fileIndexPairs;
  }
}
