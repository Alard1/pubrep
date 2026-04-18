package gui.panel.buttons;

import buildingblocks.options.ColorOption;

public interface ColorablePanel<T> {

    public void hitColorRefreshButton();

    /**
     * @param colorOption the currentColorType to set
     */
    void setCurrentColorType(ColorOption<T> colorOption);

    public void selectNewColor(ColorOption<T> selectedOption);
}
