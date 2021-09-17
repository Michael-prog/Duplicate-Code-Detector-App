package tech.sk3p7ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicateCodeDetector {
  private static final Logger logger = LoggerFactory.getLogger(DuplicateCodeDetector.class);
  public static void main(String[] args) {
    logger.info("Hello world!");
    logger.error("Hello world!");
  }
}
