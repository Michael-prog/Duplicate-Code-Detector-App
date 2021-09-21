package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sk3p7ic.detector.SourceFile;

import javax.swing.plaf.synth.SynthRootPaneUI;
import java.util.Map;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);
  public static void main(String[] args) {
    //logger.info("Hello world!");
    //logger.error("Hello world!");
    SourceFile sourceFile = new SourceFile("C:\\Users\\joshu\\IdeaProjects\\Duplicate-Code-Detector-App\\src\\main\\java\\tech\\sk3p7ic\\DuplicateCodeDetector.java");
    Map<Integer, String> lines = sourceFile.getClassFromFile();
    for (int lineIndex : lines.keySet()) {
      System.out.println((lineIndex + 1) + ":\t" + lines.get(lineIndex));
    }
  }
}
