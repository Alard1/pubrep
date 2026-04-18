package buildingblocks;

import model.TaskPlus;
import model.TaskSeriesCollectionPlus;
import model.TaskSeriesPlus;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupToTaskSeriesMapper<T extends TaskPlus> {

    public Pair<TaskSeriesCollectionPlus<T>, TaskSeriesCollectionPlus<T>>
            createLinkedDatasetsForPlot(
                    Map<String, Set<Set<T>>> leftData, Map<String, Set<Set<T>>> rightData) {

        Set<String> groupNames = new HashSet<>();

        groupNames.addAll(leftData.keySet());
        groupNames.addAll(rightData.keySet());

        List<String> names = new ArrayList<>(groupNames);
        names.sort(Comparator.naturalOrder());

        List<TaskSeriesPlus> leftSeries = new ArrayList<>();
        List<TaskSeriesPlus> rightSeries = new ArrayList<>();

        for (String name : names.reversed()) {

            Set<Set<T>> singleGroupDataLeft = leftData.get(name);
            Set<Set<T>> singleGroupDataRight = rightData.get(name);

            int parallelLeft = singleGroupDataLeft.size();
            int parallelRight = singleGroupDataRight.size();

            int totalLeft = countNumberOfItems(singleGroupDataLeft);
            int totalRight = countNumberOfItems(singleGroupDataRight);

            String groupHeaderNameLeft = createGroupHeaderName(name, totalLeft, parallelLeft);
            String groupHeaderNameRight = createGroupHeaderName(name, totalRight, parallelRight);

            int maxParallel = Math.max(parallelLeft, parallelRight);

            List<TaskSeriesPlus> left =
                    createTaskSeriesListForOneGroup(
                            singleGroupDataLeft, groupHeaderNameLeft, maxParallel);

            List<TaskSeriesPlus> right =
                    createTaskSeriesListForOneGroup(
                            singleGroupDataRight, groupHeaderNameRight, maxParallel);

            leftSeries.addAll(left);
            rightSeries.addAll(right);
        }

        TaskSeriesCollectionPlus<T> leftCollection = createCollection(leftSeries);
        TaskSeriesCollectionPlus<T> rightCollection = createCollection(rightSeries);

        return ImmutablePair.of(leftCollection, rightCollection);
    }

    private TaskSeriesCollectionPlus<T> createCollection(List<TaskSeriesPlus> taskSeriesList) {

        TaskSeriesCollectionPlus<T> tsc = new TaskSeriesCollectionPlus<T>();

        taskSeriesList.forEach(tsc::add);

        return tsc;
    }

    private int countNumberOfItems(Set<Set<T>> singleGroupData) {

        int size = 0;
        for (Set<T> set : singleGroupData) {
            size += set.size();
        }

        return size;
    }

    public TaskSeriesCollectionPlus<T> createDatasetForPlot(Map<String, Set<Set<T>>> data) {

        Set<String> groupNames = new HashSet<>(data.keySet());

        List<String> names = new ArrayList<>(groupNames);
        names.sort(Comparator.naturalOrder());

        List<TaskSeriesPlus> series = new ArrayList<>();

        for (String name : names.reversed()) {

            Set<Set<T>> singleGroupDataLeft = data.get(name);

            int parallelLeft = singleGroupDataLeft.size();

            int totalLeft = countNumberOfItems(singleGroupDataLeft);

            String groupHeaderNameLeft = createGroupHeaderName(name, totalLeft, parallelLeft);

            List<TaskSeriesPlus> left =
                    createTaskSeriesListForOneGroup(
                            singleGroupDataLeft, groupHeaderNameLeft, parallelLeft);

            series.addAll(left);
        }

        return createCollection(series);

        //		tsc.setData(items);
    }

    private String createGroupHeaderName(String groupName, int total, int parallel) {
        return groupName + " (" + total + "|" + parallel + ")";
    }

    private List<TaskSeriesPlus> createTaskSeriesListForOneGroup(
            Set<Set<T>> allTasksInGroupSplitIntoRows, String groupHeaderName, int size) {

        List<Set<T>> groupInList = new ArrayList<>();

        groupInList.addAll(allTasksInGroupSplitIntoRows);

        List<TaskSeriesPlus> taskSeriesList = new ArrayList<>();

        int delta = size - groupInList.size();

        int j = 0;

        for (int i = 0; i < delta; i++) {

            TaskSeriesPlus taskserie = new TaskSeriesPlus();

            if (j++ == 0) {
                taskserie.setHeaderRow(true);
                taskserie.setHeader(groupHeaderName);
            }

            taskSeriesList.add(taskserie);
        }

        for (Set<T> set : groupInList.reversed()) {

            TaskSeriesPlus taskserie = new TaskSeriesPlus();

            if (j++ == 0) {
                taskserie.setHeaderRow(true);
                taskserie.setHeader(groupHeaderName);
            }

            taskSeriesList.add(taskserie);

            set.forEach(taskserie::add);
        }

        return taskSeriesList.reversed();
    }
}
