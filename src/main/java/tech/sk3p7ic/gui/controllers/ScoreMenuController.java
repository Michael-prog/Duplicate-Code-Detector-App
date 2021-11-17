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
  }
}
