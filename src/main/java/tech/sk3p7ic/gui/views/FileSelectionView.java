package tech.sk3p7ic.gui.views;

import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.res.CenteredMenuView;
import tech.sk3p7ic.gui.res.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class FileSelectionView extends CenteredMenuView {
  /**
   * Sets the mode to display the menu that allows the user to choose either a project file or new file(s).
   * <p>
   * MODE_SELECT_NEW_FILES sets the menu into a mode to choose new file(s) to scan.
   * MODE_SELECT_PROJECT   sets the menu into a mode to choose a project file from a previous scan to load.
   */
  public enum ViewModes {
    MODE_SELECT_NEW_FILES,
    MODE_SELECT_PROJECT
  }

  private JButton advanceButton; // The "Continue" button
  private JButton cancelButton; // The "Cancel" button
  private ViewModes viewMode; // The mode this this menu will be set to

  /**
   * Creates a new JPanel using the CenteredMenuView. This class allows the user to choose the files that will be used
   * by the program, either being a project file or a new file(s) to scan.
   *
   * @param viewMode  The mode that this menu will be set to.
   * @param rootFrame The root JFrame that this JPanel will be displayed on.
   */
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
