package gui.panel.listener;

import gui.panel.buttons.GanttChartPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.entity.XYItemEntity;

/**
 * @author alsnippe The GanttChartListener provides the main functions for a listener connected to a
 *     gantt chart. It sets up basic (empty) method for what to do when a gantt chart element is
 *     clicked or hovered over. The methods should be overridden by sub classes
 * @param <T>
 */
public abstract class GanttChartListener<T> implements ChartMouseListener {

    protected GanttChartPanel<T> panel;

    public GanttChartListener(GanttChartPanel<T> gcm) {
        super();
        this.panel = gcm;
    }

    @Override
    public final void chartMouseClicked(ChartMouseEvent event) {

        ChartEntity entity = event.getEntity();

        if (entity instanceof XYItemEntity) {

            /*
             * an event item in the gantt chart has been clicked
             */
            clickBlock(event);

        } else if (entity instanceof LegendItemEntity) {

            /*
             * an item in the legend has been clicked
             */

            clickLegend(event);

        } else {

            /* a click has taken place, but it was not a block nor a legend item */
            clickNonBlockNonLegend(event);
        }
    }

    @Override
    public final void chartMouseMoved(ChartMouseEvent event) {

        ChartEntity entity = event.getEntity();

        if (entity instanceof LegendItemEntity) {

            hoverLegend(event);

        } else if (entity instanceof XYItemEntity) {

            hoverBlock(event);

        } else {

            hoverNonBlockNonLegend(event);
        }
    }

    /**
     * describes what should happen when a block in the connected gantt chart manager is clicked
     *
     * @param event
     */
    protected void clickBlock(ChartMouseEvent event) {}

    /**
     * describes what should happen when a legend item in the connected gantt chart manager is
     * clicked
     *
     * @param event
     */
    protected void clickLegend(ChartMouseEvent event) {}

    /**
     * describes what should happen when an item in the gantt chart manager is clicked which is not
     * a legend item, nor a block in the chart
     *
     * @param event
     */
    protected void clickNonBlockNonLegend(ChartMouseEvent event) {}

    protected void hoverLegend(ChartMouseEvent event) {}

    protected void hoverBlock(ChartMouseEvent event) {}

    protected void hoverNonBlockNonLegend(ChartMouseEvent event) {}
}
