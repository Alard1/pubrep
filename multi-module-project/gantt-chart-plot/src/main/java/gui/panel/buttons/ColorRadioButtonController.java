package gui.panel.buttons;

import buildingblocks.options.ColorOption;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ColorRadioButtonController<T> implements IRadioButtonController {

    private final ColorablePanel<T> panel;
    private final Map<String, ColorOption<T>> optionsByName;

    public ColorRadioButtonController(ColorablePanel<T> panel, List<ColorOption<T>> options) {
        this.panel = panel;
        this.optionsByName =
                options.stream()
                        .collect(
                                Collectors.toMap(
                                        ColorOption<T>::getName,
                                        Function.identity(),
                                        (x, y) -> y,
                                        LinkedHashMap::new));
    }

    @Override
    public void updateSelectedItem(String option) {

        ColorOption<T> selectedOption = optionsByName.get(option);

        panel.selectNewColor(selectedOption);
    }

    @Override
    public void hitOptionalButton() {

        panel.hitColorRefreshButton();
    }
}
