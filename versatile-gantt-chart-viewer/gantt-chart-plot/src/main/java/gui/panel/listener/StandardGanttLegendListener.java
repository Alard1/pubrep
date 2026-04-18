package gui.panel.listener;

import gui.panel.buttons.GanttChartPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.LegendItemEntity;

public class StandardGanttLegendListener<T> extends GanttChartListener<T> {

    public StandardGanttLegendListener(GanttChartPanel<T> gcm) {
        super(gcm);
    }

    @Override
    protected void hoverLegend(ChartMouseEvent event) {
        ChartEntity entity = event.getEntity();
        LegendItemEntity legendItemEntity = (LegendItemEntity) entity;
        this.panel.highlight(legendItemEntity.getURLText());
        this.panel.recolorLegend();
        this.panel.fireChange();
    }

    @Override
    protected void hoverNonBlockNonLegend(ChartMouseEvent event) {
        this.panel.returnToColorType();
        this.panel.recolorLegend();
        this.panel.fireChange();
    }
}
