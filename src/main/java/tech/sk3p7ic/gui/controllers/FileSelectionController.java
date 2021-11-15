package tech.sk3p7ic.gui.controllers;

import tech.sk3p7ic.gui.res.AppColors;
import tech.sk3p7ic.gui.res.CenteredMenuView;
import tech.sk3p7ic.gui.views.FileSelectionView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class FileSelectionController extends FileSelectionView {
  private JFrame rootFrame; // The root frame for the app
  private JButton advanceButton; // The "Continue" button
  private JButton cancelButton; // The "Cancel" button
  private JButton file1Button; // To select the first file to use
  private JButton file2Button; // To select the second file to use
  private JButton projectFileButton; // To select a project file
  private File file1; // The File for the first button
  private File file2; // The File for the second button
  private File projectFile; // The File for the .dcd project
  private CenteredMenuView previousView; // The previous JPanel before this one

  public FileSelectionController(ViewModes viewMode, JFrame rootFrame, CenteredMenuView previousView) {
    super(viewMode, rootFrame);
    this.rootFrame = rootFrame;
    this.previousView = previousView;
    // Get the components from the superclass
    advanceButton = super.getAdvanceButton();
    cancelButton = super.getCancelButton();
    file1Button = super.getFile1Button();
    file2Button = super.getFile2Button();
    projectFileButton = super.getProjectFileButton();
  }

  public void addScanFileButtonHandlers() {
    // Create the FileChooser for selecting files
    JFileChooser fileChooser = new JFileChooser();
    // Create the filter for .java files
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Files", "java");
    fileChooser.setFileFilter(filter); // Set the filter
    file1Button.addActionListener(actionEvent -> { // If the button is clicked
      int returnValue = fileChooser.showOpenDialog(null); // Show the dialog to select a file
      if (returnValue == JFileChooser.APPROVE_OPTION) { // If a file was selected
        file1 = new File(fileChooser.getSelectedFile().getAbsolutePath()); // Get the absolute path and open file
        file1Button.setText(fileChooser.getSelectedFile().getName()); // Show the name of the file in the button
      }
    });
    file2Button.addActionListener(actionEvent -> {
      int returnValue = fileChooser.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        file2 = new File(fileChooser.getSelectedFile().getAbsolutePath());
        file2Button.setText(fileChooser.getSelectedFile().getName());
      }
    });
  }

  public void addProjectFileButtonHandler() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("DCD Project Files", "dcd");
    fileChooser.setFileFilter(filter);
    projectFileButton.addActionListener(actionEvent -> {
      int returnValue = fileChooser.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        projectFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
        projectFileButton.setText(fileChooser.getSelectedFile().getName());
      }
    });
  }

  public void addOtherButtonHandlers() {
    advanceButton.addActionListener(actionEvent -> {
      if (super.getViewMode() == ViewModes.MODE_SELECT_NEW_FILES) {
        if (file1 == null || file2 == null) { // if both files have not been set yet
          JLabel errorLabel = new JLabel("<html><p><strong>PLEASE SELECT BOTH FILES.</strong></p></html>");
          errorLabel.setForeground(new Color(AppColors.ACCENT_0.getColor()));
          super.add(errorLabel, super.getConstraints());
          super.show();
        } else {
          System.out.println("I'm gonna do something.");
        }
      } else {
        if (projectFile == null) { // if both files have not been set yet
          JLabel errorLabel = new JLabel("<html><p><strong>PLEASE SELECT BOTH FILES.</strong></p></html>");
          errorLabel.setForeground(new Color(AppColors.ACCENT_0.getColor()));
          super.add(errorLabel, super.getConstraints());
          super.show();
        } else {
          System.out.println("Gonna do something cool");
        }
      }
    });
    cancelButton.addActionListener(actionEvent -> {
      this.hide();
      previousView.show();
    });
  }
}
