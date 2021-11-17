package tech.sk3p7ic.gui.views;

import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.res.CenteredMenuView;
import tech.sk3p7ic.gui.res.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends CenteredMenuView {
  private JButton startDetectionButton; // "Start New Detection" button
  private JButton loadProjectButton; // "Load DCD Project" button

  /**
   * Creates a new JPanel using the CenteredMenuView. This class allows for the creation of the main menu that the
   * program will load into on GUI mode.
   *
   * @param rootFrame The root JFrame that this JPanel will be displayed on.
   */
  public MainMenuView(JFrame rootFrame) {
    super(rootFrame, rootFrame.getTitle()); // Keep the title the same
    super.addTitleLabel("Duplicate Code Detector");
    this.add(createMenuPanel(), super.getConstraints());
    // Add credits label
    JLabel creditsLabel = new JLabel("<html><p><em>Joshua Ibrom (sk3p7ic), Mitchell Ludolf</em></p></html>");
    creditsLabel.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
    super.gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
    super.gridBagConstraints.weighty = 0;
    super.gridBagConstraints.fill = GridBagConstraints.PAGE_END;
    this.add(creditsLabel, super.getConstraints());
  }

  @Override
  public JPanel createMenuPanel() {
    super.setConstraintsForMenu(); // Change the constraints to use the menu
    // Create the panel that will store the buttons
    JPanel buttonsPanel = new JPanel(new GridBagLayout());
    buttonsPanel.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    // Create the buttons as RoundedButtons and add
    startDetectionButton = new RoundedButton("Start New Detection", true, 20);
    loadProjectButton = new RoundedButton("Load DCD Project", false, 20);
    buttonsPanel.add(startDetectionButton, super.getConstraints());
    buttonsPanel.add(loadProjectButton, super.getConstraints());
    return buttonsPanel;
  }

  public JButton getStartDetectionButton() {
    return startDetectionButton;
  }

  public JButton getLoadProjectButton() {
    return loadProjectButton;
  }
}
