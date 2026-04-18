package gui.panel.buttons;

import buildingblocks.DataContainer;
import buildingblocks.ScrollFunctionCalculator;
import buildingblocks.options.ScrollOption;

import model.TaskPlus;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScrollController<T extends TaskPlus> implements IScrollController, Observable {

    protected final List<Observer> observers = new ArrayList<>();
    protected final DataContainer<T> container;
    private final ScrollFunctionCalculator calculator = new ScrollFunctionCalculator();
    private final Map<String, ScrollOption<T>> optionsByName;
    private List<T> memorySet = null;

    public ScrollController(DataContainer<T> container, List<ScrollOption<T>> options) {

        this.container = container;
        this.optionsByName =
                options.stream()
                        .collect(
                                Collectors.toMap(
                                        ScrollOption<T>::getName,
                                        Function.identity(),
                                        (x, y) -> y,
                                        LinkedHashMap::new));
    }

    public List<Pair<String, List<String>>> initialize() {

        this.memorySet = container.getWorkingSet();
        return this.clickResetButton();
    }

    /**
     * function that should only be called by the scroll panel that is linked to this controller. it
     * is used to initialize that scroll panel. it only returns the possible values any of the
     * dimensions in the scroll panel can take.
     */
    @Override
    public List<Pair<String, List<String>>> clickResetButton() {

        Pair<List<T>, List<Pair<ScrollOption<T>, List<String>>>> analysisResult =
                calculator.determineInitialStateInformationAndDataSet(optionsByName, memorySet);

        return handleResult(analysisResult);
    }

    @Override
    public List<Pair<String, List<String>>> tellAboutStateChange(
            List<Pair<String, String>> newState, String nameOfDimensionThatWasJustChanged) {

        List<Pair<ScrollOption<T>, String>> currentStateByScrollOption = new ArrayList<>();

        for (Pair<String, String> p : newState) {

            currentStateByScrollOption.add(
                    ImmutablePair.of(optionsByName.get(p.getLeft()), p.getRight()));
        }

        Pair<List<T>, List<Pair<ScrollOption<T>, List<String>>>> analysisResult =
                calculator.determineNewStateInformationAndDataset(
                        currentStateByScrollOption, memorySet);

        return handleResult(analysisResult);
    }

    private List<Pair<String, List<String>>> handleResult(
            Pair<List<T>, List<Pair<ScrollOption<T>, List<String>>>> analysisResult) {

        List<T> data = analysisResult.getLeft();

        container.updateWorkingSet(data);

        List<Pair<ScrollOption<T>, List<String>>> possibleValuesByOption =
                analysisResult.getRight();

        List<Pair<String, List<String>>> possibleValuesByOptionName = new ArrayList<>();

        for (Pair<ScrollOption<T>, List<String>> p : possibleValuesByOption) {

            possibleValuesByOptionName.add(ImmutablePair.of(p.getLeft().getName(), p.getRight()));
        }

        this.updateObservers();

        return possibleValuesByOptionName;
    }

    @Override
    public void addObserver(Observer observer) {

        this.observers.add(observer);
    }

    @Override
    public void updateObservers() {

        this.observers.forEach(Observer::update);
    }

    @Override
    public void removeObserver(Observer observer) {

        this.observers.remove(observer);
    }
}
