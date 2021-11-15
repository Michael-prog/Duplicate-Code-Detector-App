package tech.sk3p7ic.gui.views;

import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.res.CenteredMenuView;
import tech.sk3p7ic.gui.res.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class FileSelectionView extends CenteredMenuView {
  /**
   * Sets the mode to display the menu that allows the user to choose either a project file or new file(s).
   *
   * MODE_SELECT_NEW_FILES sets the menu into a mode to choose new file(s) to scan.
   * MODE_SELECT_PROJECT   sets the menu into a mode to choose a project file from a previous scan to load.
   */
  public enum ViewModes {
    MODE_SELECT_NEW_FILES,
    MODE_SELECT_PROJECT
  }

  private JButton advanceButton; // The "Continue" button
  private JButton cancelButton; // The "Cancel" button
  private JButton file1Button; // To select the first file to use
  private JButton file2Button; // To select the second file to use
  private JButton projectFileButton; // To select a project file
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
    super.addTitleLabel("Duplicate Code Detector" + (
        (viewMode == ViewModes.MODE_SELECT_NEW_FILES) ? " -- Select Files to Scan" : " -- Select .dcd Project File")
    );
    this.add(createMenuPanel(), super.getConstraints());
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
    selectionPanel.add(getFileInputPanel(), super.getConstraints());
    selectionPanel.add(advanceButton, super.getConstraints());
    selectionPanel.add(cancelButton, super.getConstraints());
    return selectionPanel;
  }

  private JPanel getFileInputPanel() {
    JPanel fileInputPanel = new JPanel(); // The JPanel that stores the file selection labels and buttons
    if (viewMode == ViewModes.MODE_SELECT_NEW_FILES) { // If the user is trying to scan new files
      fileInputPanel.setLayout(new GridLayout(2, 2, 20, 10)); // Set the layout for the JPanel
      fileInputPanel.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor())); // Set the background for the JPanel
      // Create labels and buttons and format them
      JLabel file1Label = new JLabel("<html><p><h2>First file:</h2></p></html>");
      file1Label.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
      file1Button = new RoundedButton("Select File 1", false, 5);
      JLabel file2Label = new JLabel("<html><p><h2>Second file:</h2></p></html>");
      file2Label.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
      file2Button = new RoundedButton("Select File 2", false, 5);
      // Add the labels and buttons
      fileInputPanel.add(file1Label);
      fileInputPanel.add(file1Button);
      fileInputPanel.add(file2Label);
      fileInputPanel.add(file2Button);
    } else { // If the user wants to select a .dcd project file
      fileInputPanel.setLayout(new GridLayout(1,2, 20, 10)); // Set the layout for the JPanel
      fileInputPanel.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor())); // Set the background for the JPanel
      // Create the label and button and format them
      JLabel projectFileLabel = new JLabel("<html><p><h2>Project File:</h2></p></html>");
      projectFileLabel.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
      projectFileButton = new RoundedButton("Select Project File", false, 5);
      // Add the label and button
      fileInputPanel.add(projectFileLabel);
      fileInputPanel.add(projectFileButton);
    }
    return fileInputPanel;
  }

  public JButton getAdvanceButton() {
    return advanceButton;
  }

  public JButton getCancelButton() {
    return cancelButton;
  }

  public JButton getFile1Button() {
    return file1Button;
  }

  public JButton getFile2Button() {
    return file2Button;
  }

  public JButton getProjectFileButton() {
    return projectFileButton;
  }

  public ViewModes getViewMode() {
    return viewMode;
  }
}
