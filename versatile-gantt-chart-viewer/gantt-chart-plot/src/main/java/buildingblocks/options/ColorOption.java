package buildingblocks.options;

import java.util.function.Function;

public class ColorOption<T> {

    private final String name;
    private final Function<T, String> function;
    private final boolean hasLegend;
    private final boolean isNumericValue;
    private boolean isHighlightedNow = false;

    public ColorOption(String name, Function<T, String> function, boolean hasLegend) {
        this(name, function, hasLegend, false);
    }

    public ColorOption(
            String name, Function<T, String> function, boolean hasLegend, boolean isNumericValue) {
        this.name = name;
        this.function = function;
        this.hasLegend = hasLegend;
        this.isNumericValue = isNumericValue;
    }

    public boolean isHighlightedNow() {
        return isHighlightedNow;
    }

    public void setHighlightedNow(boolean isHighlightedNow) {
        this.isHighlightedNow = isHighlightedNow;
    }

    public String getName() {
        return name;
    }

    public Function<T, String> getFunction() {
        return function;
    }

    public boolean isHasLegend() {
        return hasLegend;
    }

    public boolean isNumericValue() {
        return isNumericValue;
    }
}
