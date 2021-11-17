package tech.sk3p7ic.gui.res;

import tech.sk3p7ic.detector.detection.SimilarityScore;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public abstract class ScoreMenuView extends JPanel {
  private JTable scoresTable;
  private JScrollPane scoresPane;
  private JButton prevButton;
  private JButton nextButton;

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
    // Make a table of the scores
    Vector<Vector<String>> scoresVector = new Vector<>(); // Stores the properties of the score
    Vector<String> headersVector = new VectorBuilder<String>().addAll("ID", "Score", "File 1 Name", "File 2 Name",
        "File 1 Index Type", "File 2 Index Type"); // Stores the column headers for the table
    // Get the data from the scores
    for (SimilarityScore score : scoreManager.getSimilarityScoreList()) {
      scoresVector.add(new VectorBuilder<String>()
          .addItem(String.valueOf(score.scoreId))
          .addItem(String.format("%.2f", score.similarityScore)) // Add the score with 2 decimal places
          .addAll(score.sourceFile1.getName(), score.sourceFile2.getName(),
              score.fileIndexPair1.fileIndexType.toString(), score.fileIndexPair2.fileIndexType.toString())
      );
    }
    scoresTable = new JTable(scoresVector, headersVector);
    scoresTable.setSelectionBackground(new Color(AppColors.ACCENT_1.getColor())); // Change the selection color
    scoresTable.setSelectionForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    scoresTable.setRowSelectionInterval(0, 0); // Select the first row
    scoresPane = new JScrollPane(scoresTable);
    // Change the font
    scoresPane.getViewport().getView().setFont(new Font("Courier New", Font.PLAIN, 12));
    scoresPane.getViewport().getView().setBackground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    scoresPane.getViewport().getView().setForeground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    sidebarPanel.add(scoresPane);
    return sidebarPanel;
  }

  private static class VectorBuilder<T> extends Vector<T> {
    /**
     * Adds an element to the Vector and returns the current Vector.
     * Thanks to https://stackoverflow.com/q/18362150 for sharing this method of doing things.
     *
     * @param element The element to add to the Vector.
     * @return The current Vector that has been made.
     */
    public VectorBuilder<T> addItem(final T element) {
      addElement(element);
      return this;
    }

    /**
     * Takes in a variable number of elements, adds them to the Vector, and returns the current Vector.
     * @param elements The element(s) to add to the Vector.
     * @return The current Vector that has been made.
     */
    @SafeVarargs
    public final VectorBuilder<T> addAll(final T... elements) {
      for (T element : elements) addElement(element);
      return this;
    }
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
    prevButton = new RoundedButton("< Prev", false, 10);
    prevButton.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    final Font currentFont = prevButton.getFont(); // Get the current font for the button
    prevButton.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 20));
    nextButton = new RoundedButton("Next >", false, 10);
    nextButton.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 20));
    nextButton.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    // Add the buttons and return
    buttonsPanel.add(prevButton);
    buttonsPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Adds space between buttons
    buttonsPanel.add(nextButton);
    return buttonsPanel;
  }

  public abstract JPanel createMainInnerPanel(SimilarityScoreManager scoreManager, int scoreId);

  public JTable getScoresTable() {
    return scoresTable;
  }

  public JScrollPane getScoresPane() {
    return scoresPane;
  }

  public JButton getPrevButton() {
    return prevButton;
  }

  public JButton getNextButton() {
    return nextButton;
  }
}
