package tech.sk3p7ic.gui.views;

import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.res.CenteredMenuView;
import tech.sk3p7ic.gui.res.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class FileSelectionView extends CenteredMenuView {
  public enum ViewModes {
    MODE_SELECT_NEW_FILES,
    MODE_SELECT_PROJECT
  }

  private JButton advanceButton;
  private JButton cancelButton;
  private ViewModes viewMode;

  public FileSelectionView(ViewModes viewMode, JFrame rootFrame) {
    super(rootFrame, rootFrame.getTitle()); // Keep the title the same
    this.viewMode = viewMode; // Set the view mode for this instance
  }

  @Override
  public JPanel createMenuPanel() {
    super.setConstraintsForMenu(); // Change the constraints to use the menu
    // Create the panel that will store the buttons
    JPanel selectionPanel = new JPanel(new GridBagLayout());
    selectionPanel.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    // Create the buttons as RoundedButtons and add
    advanceButton = new RoundedButton("Continue", true, 20);
    cancelButton = new RoundedButton("Cancel", false, 20);
    selectionPanel.add(advanceButton, super.getConstraints());
    selectionPanel.add(cancelButton, super.getConstraints());
    return selectionPanel;
  }
}
