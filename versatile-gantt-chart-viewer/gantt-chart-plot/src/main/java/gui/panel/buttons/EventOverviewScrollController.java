package gui.panel.buttons;

import buildingblocks.DataContainer;
import buildingblocks.options.ScrollOption;

import model.TaskPlus;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventOverviewScrollController<T extends TaskPlus> extends ScrollController<T> {

    private final Function<T, String> filterFunction;

    public EventOverviewScrollController(
            DataContainer<T> container, List<ScrollOption<T>> options, Function<T, String> filter) {
        super(container, options);
        this.filterFunction = filter;
    }

    @Override
    public void updateObservers() {
        Set<String> possibleValuesInWorkingSet =
                this.container.getWorkingSet().stream()
                        .map(filterFunction)
                        .collect(Collectors.toCollection(TreeSet::new));

        for (Observer o : this.observers) {

            if (o instanceof IScrollController) {
                o.update(possibleValuesInWorkingSet);
            } else if (o instanceof ScrollButtonPanel) {
                o.update();
            } else {
                o.update();
            }
        }
    }
}
