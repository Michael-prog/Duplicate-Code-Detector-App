package tech.sk3p7ic.gui.controllers;

import tech.sk3p7ic.detector.detection.SimilarityScoreManager;
import tech.sk3p7ic.gui.views.MainScoreView;

import javax.swing.*;
import java.awt.*;

public class ScoreMenuController extends MainScoreView {
  private JFrame rootFrame; // The root frame for the app

  public ScoreMenuController(JFrame rootFrame, SimilarityScoreManager scoreManager) {
    super(rootFrame);
    this.rootFrame = rootFrame;
    super.add(createScoreSidebar(scoreManager), BorderLayout.BEFORE_LINE_BEGINS);
    addScoreScrollButtonHandlers();
  }

  public void addScoreScrollButtonHandlers() {
    // Check that we're not at either disable condition yet
    if (getScoresTable().getSelectedRow() == 0) getPrevButton().setEnabled(false);
    if (getScoresTable().getSelectedRow() == getScoresTable().getRowCount() -1) getNextButton().setEnabled(false);
    getNextButton().addActionListener(actionEvent -> {
      int currentRow = getScoresTable().getSelectedRow();
      getScoresTable().setRowSelectionInterval(currentRow + 1, currentRow + 1);
      // Check if we should disable the next button because we're at the last row
      if (getScoresTable().getSelectedRow() == getScoresTable().getRowCount() -1) getNextButton().setEnabled(false);
      // Enable the previous button if it was disabled
      if (!getPrevButton().isEnabled()) getPrevButton().setEnabled(true);
    });
    getPrevButton().addActionListener(actionEvent -> {
      int currentRow = getScoresTable().getSelectedRow();
      getScoresTable().setRowSelectionInterval(currentRow - 1, currentRow -1);
      // Check if we should disable the prev button because we're at the first row
      if (getScoresTable().getSelectedRow() == 0) getPrevButton().setEnabled(false);
      // Enable the next button if it was disabled
      if (!getNextButton().isEnabled()) getNextButton().setEnabled(true);
    });
  }
}
