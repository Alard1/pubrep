package buildingblocks.options;

import model.TaskPlus;

import java.util.function.Function;

public class ScrollOption<T extends TaskPlus> {

    private final String name;
    private final Function<T, String> function;

    private final boolean possibleToSelectAll = true; // for now this is always true

    public ScrollOption(String name, Function<T, String> function, boolean possibleToSelectAll) {
        this.name = name;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public Function<T, String> getFunction() {
        return function;
    }

    public boolean isPossibleToSelectAll() {
        return possibleToSelectAll;
    }
}
