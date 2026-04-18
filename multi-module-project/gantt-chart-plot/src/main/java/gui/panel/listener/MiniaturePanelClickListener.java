package gui.panel.listener;

import gui.panel.buttons.GanttChartPanel;
import gui.panel.buttons.ScrollButtonPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;

import java.util.function.Function;

public class MiniaturePanelClickListener<T> extends GanttChartListener<T> {

    private final ScrollButtonPanel buttonPanel;
    private final Function<T, String> function;

    public MiniaturePanelClickListener(
            GanttChartPanel<T> gcm,
            Function<T, String> function,
            ScrollButtonPanel scrollButtonPanel) {
        super(gcm);
        this.buttonPanel = scrollButtonPanel;
        this.function = function;
    }

    @Override
    protected void clickBlock(ChartMouseEvent event) {

        ChartEntity entity = event.getEntity();

        XYItemEntity jo = (XYItemEntity) entity;

        int series = jo.getSeriesIndex();
        int item = jo.getItem();

        @SuppressWarnings("unchecked")
        T t = (T) this.panel.getTsc().getSeries(series).get(item);

        String keyWord = function.apply(t);

        buttonPanel.setSelectedItem(keyWord);
    }
}
