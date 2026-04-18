package gui.panel.buttons;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IScrollController {

    public List<Pair<String, List<String>>> initialize();

    public List<Pair<String, List<String>>> clickResetButton();

    public List<Pair<String, List<String>>> tellAboutStateChange(
            List<Pair<String, String>> newState, String nameOfDimensionThatWasJustChanged);
}
