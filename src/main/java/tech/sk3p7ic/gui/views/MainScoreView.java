package tech.sk3p7ic.gui.views;

import tech.sk3p7ic.gui.res.ScoreMenuView;

import javax.swing.*;

public class MainScoreView extends ScoreMenuView {
  public MainScoreView(JFrame rootFrame) {
    super(rootFrame, rootFrame.getTitle());
  }

  @Override
  public JPanel createMainInnerPanel() {
    return null;
  }
}
