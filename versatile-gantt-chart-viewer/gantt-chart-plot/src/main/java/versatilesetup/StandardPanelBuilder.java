package versatilesetup;

import app.GenericSettings;

import buildingblocks.DataContainer;
import buildingblocks.options.ColorOption;
import buildingblocks.options.GroupOption;
import buildingblocks.options.ScrollOption;

import gui.ColorManager;
import gui.panel.buttons.*;
import gui.panel.listener.StandardGanttLegendListener;

import model.TaskPlus;
import org.jfree.chart.ChartPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import javax.swing.*;

public class StandardPanelBuilder {

    private static final Dimension framesize = new Dimension(1000, 800);

    public static <T extends TaskPlus> JSplitPane createStandardPanel(GenericSettings<T> settings) {

        ColorManager<T> standardColorManager = new ColorManager<>();

        DataContainer<T> container = settings.getContainer();
        List<ScrollOption<T>> scrollOptions = settings.getScrollOptions();
        List<GroupOption<T>> groupOptions = settings.getGroupOptions();
        List<ColorOption<T>> colorOptions = settings.getColorOptions();

        Function<T, String> tooltipFunction = settings.getToolTipFunction();

        List<String> colorOptionsAsStrings =
                colorOptions.stream().map(ColorOption::getName).collect(Collectors.toList());
        List<String> groupOptionsAsStrings =
                groupOptions.stream().map(GroupOption::getName).collect(Collectors.toList());
        List<String> scrollOptionsAsStrings =
                scrollOptions.stream().map(ScrollOption::getName).collect(Collectors.toList());

        GanttChartPanel<T> targetPanel =
                new GanttChartPanel<>(tooltipFunction, standardColorManager, "EventOverviewPanel");

        targetPanel.addChartMouseListener(new StandardGanttLegendListener<>(targetPanel));

        GroupRadioButtonController<T> groupController =
                new GroupRadioButtonController<>(container, targetPanel, groupOptions);

        StandardRadioButtonPanel groupPanel =
                new StandardRadioButtonPanel(groupController, "Group by", groupOptionsAsStrings);

        ColorRadioButtonController<T> colorController =
                new ColorRadioButtonController<>(targetPanel, colorOptions);

        StandardRadioButtonPanel colorPanel =
                new StandardRadioButtonPanel(
                        colorController, "Colors", colorOptionsAsStrings, "Refresh colors");

        ScrollController<T> scrollController = new ScrollController<>(container, scrollOptions);

        scrollController.addObserver(groupPanel);

        ScrollButtonPanel scrollPanel =
                new ScrollButtonPanel(
                        scrollController, "Scroll options", scrollOptionsAsStrings, true);

        List<JPanel> panels = new ArrayList<>();
        panels.add(scrollPanel);
        panels.add(groupPanel);
        panels.add(colorPanel);

        JPanel leftPanel = buildLeftPanel(panels);

        JPanel rightPanel = buildRightPanel(targetPanel);

        container.init();
        // initialize the scroll panel to set the chain in action
        scrollPanel.update();

        colorPanel.update();

        Dimension minimumSize = new Dimension(1, 50);

        leftPanel.setMinimumSize(minimumSize);
        rightPanel.setMinimumSize(minimumSize);

        JSplitPane eventOverviewSplitPanel =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);

        eventOverviewSplitPanel.setDividerLocation(375);

        return eventOverviewSplitPanel;
    }

    public static JPanel buildLeftPanel(List<JPanel> panels) {

        JPanel leftPanel = new JPanel(new CardLayout());

        JPanel internalPanel = new JPanel();

        internalPanel.setLayout(createLeftPanelGridBagLayout(panels.size()));

        int i = 0;

        for (JPanel panel : panels) {
            internalPanel.add(panel, createSingleGridBagConstraints(i++));
        }

        internalPanel.add(new JPanel(), createFinalGridBagConstraints());

        JScrollPane jscroll =
                new JScrollPane(
                        internalPanel,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jscroll.setMinimumSize(framesize);

        leftPanel.add(jscroll);

        return leftPanel;
    }

    private static GridBagLayout createLeftPanelGridBagLayout(int numberOfPanels) {
        GridBagLayout gbl_mainPanel = new GridBagLayout();
        gbl_mainPanel.columnWidths = new int[] {0, 0};
        gbl_mainPanel.rowHeights = new int[] {0, 0};

        double[] doubles = DoubleStream.generate(() -> 0.0).limit(numberOfPanels + 1).toArray();

        doubles[numberOfPanels] = Double.MIN_VALUE;

        gbl_mainPanel.columnWeights = new double[] {Double.MIN_VALUE};
        gbl_mainPanel.rowWeights = doubles;
        return gbl_mainPanel;
    }

    public static GridBagLayout createSingleGridBagLayout() {
        GridBagLayout gbl_mainPanel = new GridBagLayout();
        gbl_mainPanel.columnWidths = new int[] {0, 0};
        gbl_mainPanel.rowHeights = new int[] {0, 0};
        gbl_mainPanel.columnWeights = new double[] {1.0, Double.MIN_VALUE};
        gbl_mainPanel.rowWeights = new double[] {Double.MIN_VALUE};
        return gbl_mainPanel;
    }

    private static GridBagConstraints createFinalGridBagConstraints() {
        GridBagConstraints gbc_scfPanel = new GridBagConstraints();
        gbc_scfPanel.insets = new Insets(0, 0, 5, 0);
        gbc_scfPanel.fill = GridBagConstraints.REMAINDER;
        gbc_scfPanel.gridx = 0;
        return gbc_scfPanel;
    }

    public static GridBagConstraints createSingleGridBagConstraints(int i) {
        GridBagConstraints gbc_scfPanel = new GridBagConstraints();
        gbc_scfPanel.insets = new Insets(0, 0, 5, 0);
        gbc_scfPanel.fill = GridBagConstraints.BOTH;
        gbc_scfPanel.gridx = 0;
        return gbc_scfPanel;
    }

    public static JPanel buildRightPanel(ChartPanel panel) {

        JPanel rightPanel = new JPanel(new CardLayout());

        panel.setMouseWheelEnabled(true);
        panel.setInitialDelay(0);
        panel.setDismissDelay(Integer.MAX_VALUE);

        rightPanel.add(panel, "overview");

        return rightPanel;
    }
}
