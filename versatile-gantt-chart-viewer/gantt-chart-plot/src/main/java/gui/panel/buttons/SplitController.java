package gui.panel.buttons;

import buildingblocks.DataContainer;
import buildingblocks.options.ScrollOption;

import model.TaskPlus;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SplitController<T extends TaskPlus>
        implements IScrollController, Observable, Observer {

    private final List<Observer> observers = new ArrayList<>();
    private final DataContainer<T> container;
    private final TreeMap<String, List<T>> splitData;
    private final List<String> allPossibleActions;
    private final String dimensionName;

    public SplitController(DataContainer<T> container, ScrollOption<T> option) {

        this.container = container;
        this.dimensionName = option.getName();

        this.splitData = initializeSplitData(option);

        this.allPossibleActions = new ArrayList<>();
        allPossibleActions.addAll(this.splitData.keySet());
    }

    private TreeMap<String, List<T>> initializeSplitData(ScrollOption<T> option) {
        return this.container.init().stream()
                .collect(
                        Collectors.groupingBy(
                                option.getFunction(), TreeMap::new, Collectors.toList()));
    }

    @Override
    public List<Pair<String, List<String>>> clickResetButton() {

        String selection = this.splitData.firstKey();

        return buildReturnObjectForSelection(selection);
    }

    @Override
    public List<Pair<String, List<String>>> tellAboutStateChange(
            List<Pair<String, String>> newState, String nameOfDimensionThatWasJustChanged) {

        String selection = newState.get(0).getRight();

        return buildReturnObjectForSelection(selection);
    }

    private List<Pair<String, List<String>>> buildReturnObjectForSelection(String selection) {

        List<T> selectedData = this.splitData.get(selection);

        container.updateWorkingSet(selectedData);

        this.updateObservers();

        List<Pair<String, List<String>>> returnList = new ArrayList<>();
        Pair<String, List<String>> pair = ImmutablePair.of(dimensionName, allPossibleActions);
        returnList.add(pair);

        return returnList;
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void updateObservers() {
        this.observers.forEach(Observer::update);
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public List<Pair<String, List<String>>> initialize() {

        return this.clickResetButton();
    }

    public void update(TreeSet<String> strings) {

        this.allPossibleActions.removeAll(strings);
        this.allPossibleActions.addAll(strings);
    }
}
