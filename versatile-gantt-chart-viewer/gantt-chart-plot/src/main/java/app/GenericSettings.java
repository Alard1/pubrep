package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import buildingblocks.DataContainer;
import buildingblocks.options.ColorOption;
import buildingblocks.options.GroupOption;
import buildingblocks.options.ScrollOption;
import model.TaskPlus;

@Getter
@Setter
public class GenericSettings<T extends TaskPlus> {

    protected List<ColorOption<T>> colorOptions = new ArrayList<>();
    protected List<GroupOption<T>> groupOptions = new ArrayList<>();
    protected List<ScrollOption<T>> scrollOptions = new ArrayList<>();
    protected ScrollOption<T> splitOption;
    protected DataContainer<T> container;
    protected Function<T, String> toolTipFunction;

    protected Pair<Function<T, Set<Comparable<?>>>, Function<T, Comparable<?>>> linkFunctions;

    public GenericSettings() {}
}
