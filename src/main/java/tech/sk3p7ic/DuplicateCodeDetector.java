package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import tech.sk3p7ic.cli.CliOptions;
import tech.sk3p7ic.detector.Detector;
import tech.sk3p7ic.detector.files.FileIndexPair;
import tech.sk3p7ic.detector.files.SourceFile;

import java.util.List;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);

  public static void main(String[] args) {
    //logger.info("Hello world!");
    //logger.error("Hello world!");
    //SourceFile sourceFile = new SourceFile("C:\\Users\\joshu\\IdeaProjects\\Duplicate-Code-Detector-App\\src\\main\\java\\tech\\sk3p7ic\\detector\\files\\FileReader.java");
    //SourceFile sourceFile = new SourceFile("C:\\Users\\joshu\\IdeaProjects\\Duplicate-Code-Detector-App\\src\\main\\java\\tech\\sk3p7ic\\DuplicateCodeDetector.java");
    //SourceFile sourceFile = new SourceFile("C:\\Users\\Josh\\IdeaProjects\\Duplicate-Code-Detector-App\\src\\main\\java\\tech\\sk3p7ic\\detector\\files\\FileReader.java");
    //sourceFile.generateAll();
    //List<FileIndexPair> fileIndexPairs = sourceFile.getFileIndexPairs();
    //for (FileIndexPair pair : fileIndexPairs) {
    //  System.out.println(pair.fileIndexType + ": (" + (pair.indexStart + 1) + ", " + (pair.indexEnd + 1) + ")");
    //}
    CliOptions cliOptions = new CliOptions();
    new CommandLine(cliOptions).parseArgs(args);
    System.out.println("Performing detection on '" + cliOptions.file1.getAbsolutePath() + "'");
    Detector mainDetector = new Detector(cliOptions.file1, cliOptions.file1);
    mainDetector.generateSimilarityScores(mainDetector.readFilesForItems());
  }
}
