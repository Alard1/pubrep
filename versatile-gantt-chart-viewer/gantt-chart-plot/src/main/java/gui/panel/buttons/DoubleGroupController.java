package gui.panel.buttons;

import buildingblocks.DataContainer;
import buildingblocks.options.GroupOption;

import model.TaskPlus;
import model.TaskSeriesCollectionPlus;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Use this class when the radio button GUI element that is connected to this controller should
 * steer two different graphs at the same time, with two different datasets at the same time. If you
 * only want to replicate the graph (so have two graphs) that listen to the same data container, it
 * is best to use the normal GroupController
 *
 * @param <T>
 */
public class DoubleGroupController<T extends TaskPlus> extends GroupRadioButtonController<T>
        implements IRadioButtonController {

    private final GanttChartPanel<T> secondTargetPanel;
    private final DataContainer<T> secondContainer;

    public DoubleGroupController(
            DataContainer<T> container,
            GanttChartPanel<T> targetPanel,
            DataContainer<T> secondContainer,
            GanttChartPanel<T> secondTargetPanel,
            List<GroupOption<T>> options) {
        super(container, targetPanel, options);

        this.secondContainer = secondContainer;
        this.secondTargetPanel = secondTargetPanel;
    }

    @Override
    public void updateSelectedItem(String optionName) {

        GroupOption<T> currentOption = optionsByName.get(optionName);

        List<T> firstDataset = container.getWorkingSetAsNewInstances();
        List<T> secondDataset = secondContainer.getWorkingSetAsNewInstances();

        Map<String, Set<Set<T>>> firstGroups =
                mapper.createDatasetForPlot(firstDataset, currentOption.getFunction());
        Map<String, Set<Set<T>>> secondGroups =
                mapper.createDatasetForPlot(secondDataset, currentOption.getFunction());

        Pair<TaskSeriesCollectionPlus<T>, TaskSeriesCollectionPlus<T>> pair =
                groupMapper.createLinkedDatasetsForPlot(firstGroups, secondGroups);

        TaskSeriesCollectionPlus<T> leftData = pair.getLeft();
        TaskSeriesCollectionPlus<T> rightData = pair.getRight();

        leftData.setData(firstDataset);
        rightData.setData(secondDataset);

        for (GanttChartPanel<T> p : targetPanel) {
            p.redraw(leftData);
        }
        secondTargetPanel.redraw(rightData);
    }
}
