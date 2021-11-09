package tech.sk3p7ic.gui.views;

import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.res.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

  public MainView() {
    this.setTitle("Duplicate Code Detector");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.getContentPane().setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    this.add(new MainMenuPane());
    this.pack();
    this.setLocationRelativeTo(null);
    this.setSize(1280, 780);
    this.setVisible(true);
  }

  private static class MainMenuPane extends JPanel {
    /**
     * Creates the main menu for the Duplicate Code Detector.
     * Some code courtesy of this comment on Stack Overflow: https://stackoverflow.com/a/42965055
     */
    public MainMenuPane() {
      this.setLayout(new GridBagLayout());
      this.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
      GridBagConstraints bagConstraints = new GridBagConstraints(); // Constraints for the layout
      // Set the width and anchor for components
      bagConstraints.gridwidth = GridBagConstraints.REMAINDER;
      bagConstraints.anchor = GridBagConstraints.NORTH;
      // Create the title label and add it to the panel
      JLabel titleLabel = new JLabel("<html><h1><strong>Duplicate Code Detector</strong></h1></html>");
      titleLabel.setForeground(new Color(AppColors.MAIN_FG_DARK.getColor()));
      this.add(titleLabel, bagConstraints);
      // Change the constraints to center the text
      bagConstraints.anchor = GridBagConstraints.CENTER;
      bagConstraints.fill = GridBagConstraints.HORIZONTAL;
      // Create the panel that will store the buttons
      JPanel buttonsPanel = new JPanel(new GridBagLayout());
      buttonsPanel.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
      bagConstraints.insets = new Insets(5, 5, 5, 5); // Used for spacing
      // Create the buttons as RoundedButtons and add
      JButton startDetectionButton = new RoundedButton("Start New Detection", true, 20);
      JButton loadProjectButton = new RoundedButton("Load DCD Project", false, 20);
      loadProjectButton.addActionListener(actionEvent -> System.out.println("Feature not yet added."));
      buttonsPanel.add(startDetectionButton, bagConstraints);
      buttonsPanel.add(loadProjectButton, bagConstraints);
      bagConstraints.weighty = 1;
      this.add(buttonsPanel, bagConstraints);
    }
  }
}
