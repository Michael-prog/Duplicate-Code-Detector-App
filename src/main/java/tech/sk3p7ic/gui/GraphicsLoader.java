package tech.sk3p7ic.gui;

import tech.sk3p7ic.gui.controllers.MainMenuController;
import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.views.FileSelectionView;
import tech.sk3p7ic.gui.views.MainMenuView;

import javax.swing.*;
import java.awt.*;

public class GraphicsLoader extends JFrame {

  /**
   * Creates a new GraphicsLoader object. Essentially, this is the root JFrame for the application.
   */
  private GraphicsLoader() {
    this.setTitle("Duplicate Code Detector"); // Set frame (window) title
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow the frame to close on exit
    this.setResizable(false); // Prevent resizing the frame
    this.getContentPane().setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    this.setLocationRelativeTo(null);
    this.setVisible(true); // Show the frame
    this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH); // Maximize the frame
  }

  /**
   * Starts the Graphical User Interface (GUI) for the program.
   */
  public static void start() {
    GraphicsLoader rootFrame = new GraphicsLoader(); // Get the root frame
    MainMenuController mainMenuController = new MainMenuController(rootFrame);
    // Add handlers for the buttons
    mainMenuController.add_startDetectionButton_handler();
    mainMenuController.add_loadProjectButton_handler();
    rootFrame.add(mainMenuController); // Show the main menu
  }
}
