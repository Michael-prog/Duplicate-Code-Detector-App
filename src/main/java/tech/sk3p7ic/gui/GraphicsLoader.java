package tech.sk3p7ic.gui;

import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.views.MainMenuView;

import javax.swing.*;
import java.awt.*;

public class GraphicsLoader extends JFrame {

  private GraphicsLoader() {
    this.setTitle("Duplicate Code Detector");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.getContentPane().setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);
  }

  /**
   * Starts the Graphical User Interface (GUI) for the program.
   */
  public static void start() {
    GraphicsLoader rootFrame = new GraphicsLoader();
    rootFrame.add(new MainMenuView(rootFrame));
  }
}
