package tech.sk3p7ic.gui.res;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;
import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ScoreDisplayPanel extends JPanel {
  private final int fileNumber; // Stores whether this pane is for the first or second file in a SimilarityScore
  private final String filename;
  private final String fileIndexType;
  private final SimilarityScore score;
  private final Map<Integer, String> scoreFileLines;

  public ScoreDisplayPanel(SimilarityScoreManager scoreManager, int scoreId, int filenumber)
      throws IllegalArgumentException {
    score = scoreManager.getSimilarityScoreList().get(scoreId); // Get the score
    if (filenumber != 1 && filenumber != 2)
      throw new IllegalArgumentException("Invalid number. Must be 1 or 2.");
    this.fileNumber = filenumber;
    // Get the lines from the source file
    scoreFileLines = scoreManager.getSimilarityScoreLines(scoreId).get(filenumber - 1);
    // Get the filename
    filename = (filenumber == 1) ? score.sourceFile1.getName() : score.sourceFile2.getName();
    // Get the FileIndexType
    fileIndexType = (filenumber == 1) ? score.fileIndexPair1.fileIndexType.toString()
                                      : score.fileIndexPair2.fileIndexType.toString();
    this.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    this.add(getFileTitleLabel());
    this.add(getTextPane());
  }

  public RTextScrollPane getTextPane() {
    RSyntaxTextArea textArea = new RSyntaxTextArea(60, 120);
    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    textArea.setCodeFoldingEnabled(true);
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Integer, String> entry : scoreFileLines.entrySet()) {
      sb.append(entry.getValue()).append("\n");
    }
    textArea.setText(sb.toString());
    textArea.setEditable(false);
    RTextScrollPane scrollPane = new RTextScrollPane(textArea);
    scrollPane.getGutter().setLineNumberingStartIndex((fileNumber == 1) ? score.fileIndexPair1.indexStart + 1
                                                                        : score.fileIndexPair2.indexStart + 1);
    return scrollPane;
  }

  public JLabel getFileTitleLabel() {
    JLabel titleLabel = new JLabel();
    titleLabel.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    int lineStart = (fileNumber == 1) ? score.fileIndexPair1.indexStart + 1 : score.fileIndexPair2.indexStart + 1;
    int lineEnd = (fileNumber == 1) ? score.fileIndexPair1.indexEnd + 1 : score.fileIndexPair2.indexEnd + 1;
    String titleText = String.format("%s (%5d, %-5d) :: %s -- %.2f", filename, lineStart, lineEnd, fileIndexType,
        score.similarityScore);
    titleLabel.setText("<html><h2>" + titleText + "</h2></html>");
    return titleLabel;
  }
}
