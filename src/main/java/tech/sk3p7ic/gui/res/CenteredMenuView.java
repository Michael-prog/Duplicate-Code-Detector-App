package tech.sk3p7ic.gui.res;

import javax.swing.*;
import java.awt.*;

public abstract class CenteredMenuView extends JPanel {
  private GridBagConstraints gridBagConstraints;

  public CenteredMenuView(JFrame rootFrame, String titleText) {
    rootFrame.setTitle(titleText);
    this.setLayout(new GridBagLayout());
    this.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    gridBagConstraints = new GridBagConstraints();
  }

  public void addTitleLabel(String text) {
    text = "<html><strong><h1>" + text + "</h1></strong></html>"; // Format the text to be bold
    JLabel titleLabel = new JLabel(text);
    titleLabel.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor())); // Change the text color
    // Set the width and anchor for components
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    this.add(titleLabel, gridBagConstraints);
  }

  public void setConstraintsForMenu() {
    // Change the constraints to center the text
    gridBagConstraints.anchor = GridBagConstraints.CENTER;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5); // Used for spacing
    gridBagConstraints.weighty = 1;
  }

  public GridBagConstraints getConstraints() { return gridBagConstraints; }

  public abstract JPanel createMenuPanel();
}
