package gui.panel.listener;

import gui.panel.buttons.GanttChartPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;

import java.util.Set;
import java.util.function.Function;

public class GanttBlockListener<T> extends GanttChartListener<T> {

    private final GanttChartPanel<T> sourcePanel;
    private final Function<T, Set<Comparable<?>>> sourceFunction;
    private final Function<T, Comparable<?>> targetFunction;

    public GanttBlockListener(
            GanttChartPanel<T> sourcePanel,
            GanttChartPanel<T> gcm,
            Function<T, Set<Comparable<?>>> sourceFunction,
            Function<T, Comparable<?>> targetFunction) {
        super(gcm);
        this.sourcePanel = sourcePanel;
        this.sourceFunction = sourceFunction;
        this.targetFunction = targetFunction;
    }

    @Override
    protected void hoverBlock(ChartMouseEvent event) {
        ChartEntity entity = event.getEntity();

        XYItemEntity jo = (XYItemEntity) entity;

        int series = jo.getSeriesIndex();
        int item = jo.getItem();

        T t = (T) this.sourcePanel.getTsc().getSeries(series).get(item);

        Set<Comparable<?>> keyWords = sourceFunction.apply(t);

        this.panel.highlightSpecificItems(keyWords, targetFunction);
        this.panel.recolorLegend();
        this.panel.fireChange();
    }

    @Override
    protected void hoverNonBlockNonLegend(ChartMouseEvent event) {

        this.panel.stopHighlightingSpecificItems();
        this.panel.getCurrentColorType().setHighlightedNow(false);
        this.panel.recolorLegend();
        this.panel.fireChange();
    }
}
