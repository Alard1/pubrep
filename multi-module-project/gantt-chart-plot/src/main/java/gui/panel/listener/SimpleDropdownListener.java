package gui.panel.listener;

import gui.panel.buttons.GanttChartPanel;
import gui.panel.buttons.Observer;

public class SimpleDropdownListener<T> implements Observer {

    private final GanttChartPanel<T> panel;

    public SimpleDropdownListener(GanttChartPanel<T> panel) {
        this.panel = panel;
    }

    @Override
    public void update(String string) {
        this.panel.highlight(string);
        this.panel.createLegend();
        this.panel.fireChange();
    }
}
