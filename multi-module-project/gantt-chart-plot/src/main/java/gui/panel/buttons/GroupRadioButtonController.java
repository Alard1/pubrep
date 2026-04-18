package gui.panel.buttons;

import buildingblocks.BlockToGroupMapper;
import buildingblocks.DataContainer;
import buildingblocks.GroupToTaskSeriesMapper;
import buildingblocks.options.GroupOption;

import model.TaskPlus;
import model.TaskSeriesCollectionPlus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupRadioButtonController<T extends TaskPlus> implements IRadioButtonController {

    protected final List<GanttChartPanel<T>> targetPanel = new ArrayList<>();
    protected final DataContainer<T> container;
    protected final Map<String, GroupOption<T>> optionsByName;

    protected final BlockToGroupMapper<T> mapper = new BlockToGroupMapper<T>();
    protected final GroupToTaskSeriesMapper<T> groupMapper = new GroupToTaskSeriesMapper<T>();

    public GroupRadioButtonController(
            DataContainer<T> container,
            GanttChartPanel<T> targetPanel,
            List<GroupOption<T>> options) {
        this.container = container;
        this.targetPanel.add(targetPanel);
        this.optionsByName =
                options.stream()
                        .collect(
                                Collectors.toMap(
                                        GroupOption<T>::getName,
                                        Function.identity(),
                                        (x, y) -> y,
                                        LinkedHashMap::new));
    }

    @Override
    public void updateSelectedItem(String optionName) {

        GroupOption<T> currentOption = optionsByName.get(optionName);

        List<T> dataset = container.getWorkingSetAsNewInstances();

        Map<String, Set<Set<T>>> groups =
                mapper.createDatasetForPlot(dataset, currentOption.getFunction());

        TaskSeriesCollectionPlus<T> tsc = groupMapper.createDatasetForPlot(groups);

        tsc.setData(dataset);

        for (GanttChartPanel<T> s : targetPanel) {
            s.redraw(tsc);
        }
    }

    @Override
    public void hitOptionalButton() {
        // not implemented for the grouping radio button panel

    }

    public void addPanel(GanttChartPanel<T> p) {
        targetPanel.add(p);
    }
}
