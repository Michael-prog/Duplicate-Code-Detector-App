package tech.sk3p7ic.gui.res;

public enum AppColors {
  MAIN_BG_DARK(0x263238),
  MAIN_FG_DARK(0xECEFF1),
  BG_DARK_0(0x37474f),
  ACCENT_0(0xF50057),
  ACCENT_1(0xEC407A);

  private final int colorValue; // The value for the color

  AppColors(int colorValue) {
    this.colorValue = colorValue;
  }

  public int getColor() {
    return colorValue;
  }
}
