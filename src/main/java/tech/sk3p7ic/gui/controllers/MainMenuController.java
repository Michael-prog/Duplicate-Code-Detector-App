package tech.sk3p7ic.gui.controllers;

import tech.sk3p7ic.gui.res.CenteredMenuView;
import tech.sk3p7ic.gui.views.FileSelectionView;
import tech.sk3p7ic.gui.views.MainMenuView;

import javax.swing.*;

public class MainMenuController extends MainMenuView {
  private JFrame rootFrame; // The root JFrame for the application
  private JButton startDetectionButton;
  private JButton loadProjectButton;

  public MainMenuController(JFrame rootFrame) {
    super(rootFrame);
    this.rootFrame = rootFrame;
    // Get the buttons from the main menu
    startDetectionButton = super.getStartDetectionButton();
    loadProjectButton = super.getLoadProjectButton();
    // Add the handlers to the buttons
  }

  public void add_startDetectionButton_handler() {
    startDetectionButton.addActionListener(actionEvent -> {
      rootFrame.remove(this); // Remove the view that's currently being displayed
      CenteredMenuView menuView = new FileSelectionView(FileSelectionView.ViewModes.MODE_SELECT_NEW_FILES, rootFrame);
      rootFrame.add(menuView); // Add the new view
      menuView.show();
    });
  }
}
