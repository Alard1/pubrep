package buildingblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataContainer<T> {

    private final List<T> transformedStartData = new ArrayList<>();
    private final List<T> workingSet = new ArrayList<>();
    private final Function<List<T>, List<T>> cloneFunction;

    public DataContainer(List<T> reservations, Function<List<T>, List<T>> cloneFunction) {

        this.transformedStartData.addAll(reservations);
        this.cloneFunction = cloneFunction;
    }

    public void updateWorkingSet(List<T> data) {
        workingSet.clear();
        workingSet.addAll(data);
    }

    public List<T> init() {
        workingSet.clear();
        workingSet.addAll(transformedStartData);
        return new ArrayList<>(workingSet);
    }

    public List<T> getWorkingSet() {
        return new ArrayList<>(workingSet);
    }

    public List<T> getWorkingSetAsNewInstances() {

        List<T> newClones = new ArrayList<>();

        newClones.addAll(cloneFunction.apply(workingSet));

        workingSet.clear();
        workingSet.addAll(newClones);
        return workingSet;
    }
}
