package gui.panel.buttons;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ScrollControllerAdapter implements IScrollController, Observable {

    private final List<IScrollController> controllers = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public List<Pair<String, List<String>>> initialize() {
        List<Pair<String, List<String>>> returnList = new ArrayList<>();

        for (IScrollController c : controllers) {
            returnList = c.initialize();
        }

        this.updateObservers();
        return returnList;
    }

    @Override
    public List<Pair<String, List<String>>> clickResetButton() {

        List<Pair<String, List<String>>> returnList = new ArrayList<>();

        for (IScrollController c : controllers) {
            returnList = c.clickResetButton();
        }
        this.updateObservers();
        return returnList;
    }

    @Override
    public List<Pair<String, List<String>>> tellAboutStateChange(
            List<Pair<String, String>> newState, String nameOfDimensionThatWasJustChanged) {

        List<Pair<String, List<String>>> returnList = new ArrayList<>();

        for (IScrollController c : controllers) {
            returnList = c.tellAboutStateChange(newState, nameOfDimensionThatWasJustChanged);
        }
        this.updateObservers();
        return returnList;
    }

    public void addController(IScrollController c) {
        this.controllers.add(c);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void updateObservers() {

        for (Observer o : this.observers) {
            o.update();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }
}
