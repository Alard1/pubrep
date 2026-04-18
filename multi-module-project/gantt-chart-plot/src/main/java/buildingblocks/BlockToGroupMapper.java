package buildingblocks;

import model.TaskPlus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlockToGroupMapper<T extends TaskPlus> {

    public BlockToGroupMapper() {}

    public Map<String, Set<Set<T>>> createDatasetForPlot(
            List<T> items, Function<T, String> function) {

        Map<String, List<T>> mapOfGroups = splitBlocksIntoGroups(items, function);

        return processGroupsIntoRows(mapOfGroups);
    }

    private Map<String, Set<Set<T>>> processGroupsIntoRows(Map<String, List<T>> mapOfGroups) {

        TreeMap<String, Set<Set<T>>> map = new TreeMap<>();

        for (Entry<String, List<T>> entry : mapOfGroups.entrySet()) {

            String groupName = entry.getKey();
            List<T> allTasksInGroup = entry.getValue();
            Set<Set<T>> allTasksInGroupSplitIntoRows = this.separateRows(allTasksInGroup);

            map.put(groupName, allTasksInGroupSplitIntoRows);
        }

        return map;
    }

    private Map<String, List<T>> splitBlocksIntoGroups(
            List<T> items, Function<T, String> function) {

        Map<String, List<T>> itemsInMap =
                items.stream()
                        .collect(
                                Collectors.groupingBy(function, TreeMap::new, Collectors.toList()));

        return itemsInMap;
    }

    public Set<Set<T>> separateRows(List<T> items) {

        items.sort(Comparator.comparing(TaskPlus::getEndDate));
        items.sort(Comparator.comparing(TaskPlus::getStartDate));

        Set<Set<T>> separatedRows = new LinkedHashSet<>();
        Set<T> innerList = new LinkedHashSet<>();
        Set<T> processedList = new LinkedHashSet<>();
        Integer numberOfEventBlocks = items.size();
        innerList.add(items.getFirst());
        processedList.add(items.getFirst());
        LocalDate ld = items.getFirst().getEndDate();

        while (processedList.size() < numberOfEventBlocks) {

            boolean ebFound = false;
            for (T eb : items) {
                if (processedList.contains(eb)) {
                    continue;
                }

                if (eb.getStartDate().isAfter(ld) || eb.getStartDate().isEqual(ld)) {
                    ebFound = true;
                    ld = eb.getEndDate();
                    processedList.add(eb);
                    innerList.add(eb);
                    break;
                }
            }

            if (!ebFound) {
                separatedRows.add(innerList);
                innerList = new LinkedHashSet<>();

                for (T eb : items) {

                    if (processedList.contains(eb)) {
                        continue;
                    }

                    innerList.add(eb);
                    processedList.add(eb);
                    ld = eb.getEndDate();
                    break;
                }
            }
        }

        separatedRows.add(innerList);

        return separatedRows;
    }
}
