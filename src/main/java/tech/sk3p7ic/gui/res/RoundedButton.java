package tech.sk3p7ic.gui.res;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RoundedButton extends JButton {
  private final String originalText; // Stores the text the button was initialized with

  /**
   * Creates a new JButton with rounded corners.
   *
   * @param text       String of text to display on the button.
   * @param isSelected Boolean if the button is currently selected.
   * @param radius     Integer of the radius to round the corners by.
   */
  public RoundedButton(String text, boolean isSelected, int radius) {
    originalText = text;
    this.setSelected(isSelected);
    if (this.isSelected())
      this.setText("<html><h3><strong>" + text + "</strong></h3></html>");
    else
      this.setText(text);
    this.setForeground(new Color(AppColors.ACCENT_0.getColor()));
    this.setBackground(new Color(AppColors.MAIN_BG_DARK.getColor()));
    this.setFocusPainted(false); // Don't show the focus box
    this.setBorder(new RoundedBorder(radius));
    this.setOpaque(false);
  }

  @Override
  public void setSelected(boolean b) {
    super.setSelected(b);
    if (this.isSelected())
      this.setText("<html><h3><strong>" + originalText + "</strong></h3></html>");
    else
      this.setText(originalText);
  }

  /**
   * Sets the radius of the corners to a given value.
   *
   * @param radius Integer of the radius to round the corners by.
   */
  public void setRadius(int radius) {
    this.setBorder(new RoundedBorder(radius));
  }

  private static class RoundedBorder implements Border {
    // Concept for this class courtesy of https://stackoverflow.com/a/3634480
    private int radius; // The radius for the border

    public RoundedBorder(int radius) {
      this.radius = radius;
    }

    public void setRadius(int radius) {
      this.radius = radius;
    }

    @Override
    public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
      graphics.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component component) {
      return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    @Override
    public boolean isBorderOpaque() {
      return true;
    }
  }
}
