package buildingblocks;

import buildingblocks.options.ScrollOption;

import model.TaskPlus;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class does the difficult calculation stuff related to filtering and data set manipulation
 */
public class ScrollFunctionCalculator {

    public static final String ALL = "All";

    /**
     * @param <T>
     * @param options
     * @param inputDataset
     * @return a complex pair of data
     */
    public <T extends TaskPlus>
            Pair<List<T>, List<Pair<ScrollOption<T>, List<String>>>>
                    determineInitialStateInformationAndDataSet(
                            Map<String, ScrollOption<T>> options, List<T> inputDataset) {

        Pair<List<T>, List<Pair<ScrollOption<T>, List<String>>>> returnObject;

        List<Pair<ScrollOption<T>, List<String>>> possibleValuesPerOption =
                determinePossibleValuesPerOption(options, inputDataset);

        returnObject = ImmutablePair.of(inputDataset, possibleValuesPerOption);

        return returnObject;
    }

    public <T extends TaskPlus>
            Pair<List<T>, List<Pair<ScrollOption<T>, List<String>>>>
                    determineNewStateInformationAndDataset(
                            List<Pair<ScrollOption<T>, String>> currentStateByScrollOption,
                            List<T> inputDataset) {

        return determinePossibleValuesPerOptionGivenOtherFields(
                currentStateByScrollOption, inputDataset);
    }

    private <T extends TaskPlus>
            List<Pair<ScrollOption<T>, List<String>>> determinePossibleValuesPerOption(
                    Map<String, ScrollOption<T>> options, List<T> inputDataset) {

        List<Pair<ScrollOption<T>, List<String>>> returnlist = new ArrayList<>();

        Map<ScrollOption<T>, Set<String>> possibleValueSets = new HashMap<>();

        for (T t : inputDataset) {

            for (Entry<String, ScrollOption<T>> option : options.entrySet()) {

                String possibleValue = option.getValue().getFunction().apply(t);

                possibleValueSets
                        .computeIfAbsent(option.getValue(), v -> new TreeSet<>())
                        .add(possibleValue);
            }
        }

        for (Entry<ScrollOption<T>, Set<String>> possibleValueSet : possibleValueSets.entrySet()) {

            List<String> finalValueSet = new ArrayList<>();

            boolean isPossibleToSelectAll = possibleValueSet.getKey().isPossibleToSelectAll();

            if (isPossibleToSelectAll) {
                finalValueSet.add(ALL);
            }
            finalValueSet.addAll(possibleValueSet.getValue());

            returnlist.add(ImmutablePair.of(possibleValueSet.getKey(), finalValueSet));
        }

        return returnlist;
    }

    private <T extends TaskPlus>
            Pair<List<T>, List<Pair<ScrollOption<T>, List<String>>>>
                    determinePossibleValuesPerOptionGivenOtherFields(
                            List<Pair<ScrollOption<T>, String>> options, List<T> inputDataset) {

        List<Pair<ScrollOption<T>, List<String>>> returnList = new ArrayList<>();

        Map<ScrollOption<T>, Set<String>> possibleValueSets = new HashMap<>();

        List<T> outputDataset = new ArrayList<>();

        for (T t : inputDataset) {

            if (countsForFilterThatIsGiven(t, options)) {
                outputDataset.add(t);
            }

            for (Pair<ScrollOption<T>, String> option : options) {

                if (countsForCalculationOfBackGroundStates(t, options, option)) {

                    String possibleValue = option.getLeft().getFunction().apply(t);

                    possibleValueSets
                            .computeIfAbsent(option.getLeft(), v -> new TreeSet<>())
                            .add(possibleValue);
                }
            }
        }

        for (Entry<ScrollOption<T>, Set<String>> possibleValueSet : possibleValueSets.entrySet()) {

            List<String> finalValueSet = new ArrayList<>();

            boolean isPossibleToSelectAll = possibleValueSet.getKey().isPossibleToSelectAll();

            if (isPossibleToSelectAll) {
                finalValueSet.add(ALL);
            }
            finalValueSet.addAll(possibleValueSet.getValue());

            returnList.add(ImmutablePair.of(possibleValueSet.getKey(), finalValueSet));
        }

        return ImmutablePair.of(outputDataset, returnList);
    }

    private <T extends TaskPlus> boolean countsForCalculationOfBackGroundStates(
            T t,
            List<Pair<ScrollOption<T>, String>> options,
            Pair<ScrollOption<T>, String> option) {

        for (Pair<ScrollOption<T>, String> item : options) {

            if (item == option) {
                continue;
            }

            String filterValue = item.getRight();

            if (filterValue == null || filterValue.equals(ALL)) {
                continue;
            }

            String value = item.getLeft().getFunction().apply(t);

            if (!value.equalsIgnoreCase(filterValue)) {
                return false;
            }
        }

        return true;
    }

    private <T extends TaskPlus> boolean countsForFilterThatIsGiven(
            T t, List<Pair<ScrollOption<T>, String>> options) {

        for (Pair<ScrollOption<T>, String> item : options) {

            String filterValue = item.getRight();

            if (filterValue.equals(ALL)) {
                continue;
            }

            String value = item.getLeft().getFunction().apply(t);

            if (!value.equalsIgnoreCase(filterValue)) {
                return false;
            }
        }

        return true;
    }
}
