package tech.sk3p7ic.gui.controllers;

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
  }

  public void add_startDetectionButton_handler() {
    startDetectionButton.addActionListener(actionEvent -> {
      this.hide();
      FileSelectionController menuView = new FileSelectionController(FileSelectionView.ViewModes.MODE_SELECT_NEW_FILES, rootFrame, this);
      menuView.addScanFileButtonHandlers();
      menuView.addOtherButtonHandlers();
      rootFrame.add(menuView); // Add the new view
      menuView.show();
    });
  }

  public void add_loadProjectButton_handler() {
    loadProjectButton.addActionListener(actionEvent -> {
      this.hide();
      FileSelectionController menuView = new FileSelectionController(FileSelectionView.ViewModes.MODE_SELECT_PROJECT, rootFrame, this);
      menuView.addProjectFileButtonHandler();
      menuView.addOtherButtonHandlers();
      rootFrame.add(menuView); // Add the new view
      menuView.show();
    });
  }
}
