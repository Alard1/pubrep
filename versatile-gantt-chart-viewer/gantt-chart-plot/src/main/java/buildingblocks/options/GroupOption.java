package buildingblocks.options;

import model.TaskPlus;

import java.util.function.Function;

public class GroupOption<T extends TaskPlus> {

    private final String name;
    private final Function<T, String> function;

    public GroupOption(String name, Function<T, String> function) {
        this.name = name;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public Function<T, String> getFunction() {
        return function;
    }
}
