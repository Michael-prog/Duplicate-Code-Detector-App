package tech.sk3p7ic.gui.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sk3p7ic.detector.detection.SimilarityScoreManager;
import tech.sk3p7ic.gui.res.ScoreDisplayPanel;
import tech.sk3p7ic.gui.res.ScoreMenuView;

import javax.swing.*;

public class MainScoreView extends ScoreMenuView {
  private final static Logger logger = LoggerFactory.getLogger(MainScoreView.class);
  private JPanel innerPanel;

  public MainScoreView(JFrame rootFrame) {
    super(rootFrame, rootFrame.getTitle());
  }

  /*
  Please note that there's some methods that should be in this class but currently are in ScoreMenuView because adding
  them to this class would cause bugs with the buttons for some reason. If there's more time, those methods will be
  moved over.
   */

  @Override
  public JPanel createMainInnerPanel(SimilarityScoreManager scoreManager, int scoreId) {
    JPanel mainInnerPanel = new JPanel();
    mainInnerPanel.setLayout(new BoxLayout(mainInnerPanel, BoxLayout.LINE_AXIS));
    try {
      mainInnerPanel.add(new ScoreDisplayPanel(scoreManager, scoreId, 1));
      mainInnerPanel.add(new ScoreDisplayPanel(scoreManager, scoreId, 2));
    } catch (IllegalArgumentException e) {
      logger.error(e.getMessage());
    }
    return mainInnerPanel;
  }

  public JPanel getInnerPanel() {
    return innerPanel;
  }

  public void setInnerPanel(JPanel panel) {
    innerPanel = panel;
  }
}
