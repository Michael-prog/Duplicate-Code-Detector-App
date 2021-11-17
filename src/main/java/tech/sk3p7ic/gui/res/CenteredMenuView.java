package tech.sk3p7ic.gui.res;

import javax.swing.*;
import java.awt.*;

public abstract class CenteredMenuView extends JPanel {
  public GridBagConstraints gridBagConstraints; // Stores the constraints for the layout in this JPanel

  /**
   * Creates a new JPanel with centered content.
   *
   * @param rootFrame The root JFrame that this JPanel will be displayed on.
   * @param titleText The title of the root JFrame.
   */
  public CenteredMenuView(JFrame rootFrame, String titleText) {
    rootFrame.setTitle(titleText); // Set / change the title
    this.setLayout(new GridBagLayout()); // Set the layout
    this.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    gridBagConstraints = new GridBagConstraints(); // Create the constraints of the layout
  }

  public void addTitleLabel(String text) {
    text = "<html><strong><h1>" + text + "</h1></strong></html>"; // Format the text to be bold
    JLabel titleLabel = new JLabel(text); // Create the title label
    titleLabel.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor())); // Change the text color
    // Set the width and anchor for components
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    this.add(titleLabel, gridBagConstraints); // Add the label
  }

  /**
   * Changes the constraints for the menu. Essentially, this will ensure that the text is displayed centered in the
   * root JFrame both vertically and horizontally.
   */
  public void setConstraintsForMenu() {
    // Change the constraints to center the text
    gridBagConstraints.anchor = GridBagConstraints.CENTER;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5); // Used for spacing
    gridBagConstraints.weighty = 1;
  }

  /**
   * Gets the current constraints being used.
   *
   * @return GridBagConstraints being used to display the Components.
   */
  public GridBagConstraints getConstraints() {
    return gridBagConstraints;
  }

  /**
   * Creates the main menu for the current view.
   *
   * @return A JPanel with the content that will be centered on the root JFrame as the menu, not including the title.
   */
  public abstract JPanel createMenuPanel();
}
