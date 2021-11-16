package tech.sk3p7ic.gui.res;

import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;

import javax.swing.*;
import java.awt.*;

public abstract class ScoreMenuView extends JPanel {
  public ScoreMenuView(JFrame rootFrame, String titleText) {
    rootFrame.setTitle(titleText); // Set / change the title
    this.setLayout(new BorderLayout());
    this.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
  }

  public void addTitleLabel(String text) {
    text = "<html><strong><h1>" + text + "</h1></strong></html>"; // Format the text to be bold
    JLabel titleLabel = new JLabel(text); // Create the title label
    titleLabel.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor())); // Change the text color
    this.add(titleLabel, BorderLayout.NORTH); // Add the label
  }

  public JPanel createScoreSidebar(SimilarityScoreManager scoreManager) {
    JPanel sidebarPanel = new JPanel();
    sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.PAGE_AXIS));
    sidebarPanel.add(createScoreSidebarButtonsPanel());
    for (SimilarityScore score : scoreManager.getSimilarityScoreList()) {
      JLabel scoreLabel = new JLabel();
      scoreLabel.setForeground(new Color(AppColors.ACCENT_0.getColor()));
      scoreLabel.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
      String labelText = score.scoreId + "    " + score.similarityScore;
      scoreLabel.setText(labelText);
      sidebarPanel.add(scoreLabel);
    }
    return sidebarPanel;
  }

  private JPanel createScoreSidebarButtonsPanel() {
    JPanel buttonsPanel = new JPanel(new FlowLayout());
    JButton prevButton = new JButton("< Prev");
    JButton nextButton = new JButton("Next >");
    buttonsPanel.add(prevButton);
    buttonsPanel.add(nextButton);
    return buttonsPanel;
  }

  public abstract JPanel createMainInnerPanel();
}
