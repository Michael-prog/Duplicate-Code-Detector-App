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

  /**
   * Creates the sidebar that will display all the scores stored by the SimilarityScoreManager. This pane allows the
   * user to select the score that they would like to "dig" into.
   * @param scoreManager The SimilarityScoreManager storing the scores that will be displayed.
   * @return The sidebar.
   */
  public JPanel createScoreSidebar(SimilarityScoreManager scoreManager) {
    scoreManager.sortSimilarityScoreList(); // Sort the scores before displaying them
    JPanel sidebarPanel = new JPanel(); // The main panel that will be displayed
    sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.PAGE_AXIS)); // Set the layout
    sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add a margin
    sidebarPanel.setBackground(new Color(AppColors.BG_DARK_0.getColor())); // Change the background color
    sidebarPanel.add(createScoreSidebarButtonsPanel()); // Create the buttons to flip through scores and add
    // Get the information about SimilarityScores to display as a list in the sidebar
    DefaultListModel<String> rawScoresList = new DefaultListModel<>();
    for (SimilarityScore score : scoreManager.getSimilarityScoreList()) {
      String scoreString = String.format("| %-5d | %-5.2f | %-15s | %-15s | %-20s | %-20s |%n", score.scoreId,
          score.similarityScore, score.sourceFile1.getName(), score.sourceFile2.getName(),
          score.fileIndexPair1.fileIndexType, score.fileIndexPair2.fileIndexType);
      rawScoresList.addElement(scoreString);
    }
    // Parse the list into a JList and add to a JScrollPane so that the scores may be displayed
    JList<String> scoreList = new JList<>(rawScoresList);
    JScrollPane scoresPane = new JScrollPane(scoreList);
    // Change the font
    scoresPane.getViewport().getView().setFont(new Font("Courier New", Font.PLAIN, 12));
    sidebarPanel.add(scoresPane);
    return sidebarPanel;
  }

  /**
   * Creates the buttons that are displayed at the top of the sidebar.
   * @return A JPanel with 2 buttons to advance one score and go back one score.
   */
  private JPanel createScoreSidebarButtonsPanel() {
    JPanel buttonsPanel = new JPanel();
    // Set the layout, add a margin, and change the background
    buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
    buttonsPanel.setBackground(new Color(AppColors.BG_DARK_0.getColor()));
    // Create the buttons and set their font size to be 20
    JButton prevButton = new RoundedButton("< Prev", false, 10);
    prevButton.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    final Font currentFont = prevButton.getFont();
    prevButton.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 20));
    JButton nextButton = new RoundedButton("Next >", false, 10);
    nextButton.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 20));
    nextButton.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    // Add the buttons and return
    buttonsPanel.add(prevButton);
    buttonsPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Adds space between buttons
    buttonsPanel.add(nextButton);
    return buttonsPanel;
  }

  public abstract JPanel createMainInnerPanel();
}
