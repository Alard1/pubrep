package gui;

import buildingblocks.options.ColorOption;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorManager<T> {

    protected Color highlightColor = new Color(15, 35, 140);
    protected Color lowlightColor = new Color(233, 239, 252);
    protected Color standardColor = Color.BLACK;
    private int range = 256;
    private Color backGroundColor = new Color(247, 247, 247);
    private Color defaultColor = new Color(100, 100, 100);
    private Color gridColor = Color.gray;
    private Map<ColorOption<T>, Map<String, Color>> colorPatterns = new HashMap<>();

    public void reColorPattern(ColorOption<T> patternName) {

        if (patternName == null) {
            this.defaultColor = generateRandomColor();
        } else {

        }

        this.colorPatterns.remove(patternName);
    }

    private Color generateRandomColor() {

        int R = (int) (Math.random() * range);
        int G = (int) (Math.random() * range);
        int B = (int) (Math.random() * range);
        return new Color(R + (256 - range), G + (256 - range), B + (256 - range));
    }

    public Color getColorFromPattern(
            ColorOption<T> patternName, String colorName, String currentHighlightSubject) {

        if (patternName == null) {
            return standardColor;
        }

        if (patternName.isHighlightedNow() && currentHighlightSubject != null) {
            return colorName.equals(currentHighlightSubject) ? highlightColor : lowlightColor;
        }

        return colorPatterns
                .computeIfAbsent(patternName, v -> new HashMap<>())
                .computeIfAbsent(colorName, v -> generateRandomColor());
    }

    /**
     * @return the backGroundColor
     */
    public Color getBackGroundColor() {
        return backGroundColor;
    }

    /**
     * @param backGroundColor the backGroundColor to set
     */
    public void setBackGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    /**
     * @return the gridColor
     */
    public Color getGridColor() {
        return gridColor;
    }

    /**
     * @param gridColor the gridColor to set
     */
    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    /**
     * @return the highlightColor
     */
    public Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * @param highlightColor the highlightColor to set
     */
    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    /**
     * @return the lowlightColor
     */
    public Color getLowlightColor() {
        return lowlightColor;
    }

    /**
     * @param lowlightColor the lowlightColor to set
     */
    public void setLowlightColor(Color lowlightColor) {
        this.lowlightColor = lowlightColor;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }
}
