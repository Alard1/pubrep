package gui.panel.buttons;

import buildingblocks.XYTaskDataset;
import buildingblocks.options.ColorOption;

import gui.ColorManager;

import model.TaskSeriesCollectionPlus;
import model.TaskSeriesPlus;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.XYDataset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

public class GanttChartPanel<T> extends ChartPanel implements ColorablePanel<T> {

    private static final long serialVersionUID = 1L;
    public final String yAxisName = "Group ID";
    private final String name;
    private final Function<T, String> toolTipFunction;
    protected String highlightSubject;
    protected LegendItemCollection chartLegend;
    protected XYPlot plot;
    protected SymbolAxis yAxis;
    protected Stroke gridStroke =
            new BasicStroke(
                    0.4f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    0.4f,
                    new float[] {1f, 1f},
                    0.0f);
    protected TaskSeriesCollectionPlus<T> tsc;
    XYToolTipGenerator xyToolTipGenerator =
            new XYToolTipGenerator() {

                public String generateToolTip(XYDataset dataset, int series, int item) {

                    T tsk = (T) tsc.getSeries(series).get(item);

                    return toolTipFunction.apply(tsk);
                }
            };
    private ColorOption<T> currentColor = null;
    private ColorManager<T> colorManager;
    private MyRenderer standardRenderer;
    private QuickHighlightRenderer quickHighlightRenderer = new QuickHighlightRenderer();

    public GanttChartPanel(
            Function<T, String> toolTipFunction, ColorManager<T> colorManager, String name) {

        super(
                ChartFactory.createXYBarChart(
                        name,
                        "Date",
                        true,
                        null,
                        new XYTaskDataset(new TaskSeriesCollectionPlus<T>()),
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false));

        this.name = name;

        ((XYPlot) this.getChart().getPlot()).setFixedLegendItems(new LegendItemCollection());

        this.tsc = new TaskSeriesCollectionPlus<T>();
        this.toolTipFunction = toolTipFunction;
        this.colorManager = colorManager;

        formatChart();
        formatPlot();

        setYAxisVisible(true);
        setLegendVisible(true);
    }

    private void formatChart() {
        this.getChart().getTitle().setFont(new Font("Calibri", Font.PLAIN, 15));
        this.getChart().setBackgroundPaint(colorManager.getBackGroundColor());
        this.getChart().getLegend().setBackgroundPaint(colorManager.getBackGroundColor());
        this.getChart().getLegend().setPosition(RectangleEdge.RIGHT);
        Font labelFont = new Font("Arial", Font.PLAIN, 9);
        this.getChart().getLegend().setItemFont(labelFont);
        plot = (XYPlot) this.getChart().getPlot();
    }

    private void formatPlot() {

        yAxis = new SymbolAxis("Group ID", createYAxisTags());
        Font tickLabelFont = new Font("Arial", Font.PLAIN, 10);
        yAxis.setTickLabelFont(tickLabelFont);
        yAxis.setGridBandsVisible(false);
        plot.setRangeAxis(yAxis);

        standardRenderer = new MyRenderer();

        plot.setRenderer(standardRenderer);
        plot.setBackgroundPaint(colorManager.getBackGroundColor());
        plot.setDomainGridlinePaint(colorManager.getGridColor());
        plot.setDomainGridlineStroke(gridStroke);
        plot.setRangeGridlinesVisible(false);

        createLegend();
        setYAxisVisible(false);
        setLegendVisible(true);

        addHorizontalMarkers();
    }

    public void hitColorRefreshButton() {

        colorManager.reColorPattern(currentColor);

        createLegend();
        this.getChart().fireChartChanged();
    }

    public void createLegend() {

        chartLegend = new LegendItemCollection();

        if (currentColor == null || !currentColor.isHasLegend()) {

        } else {

            chartLegend = this.createLegendCool();
        }

        plot.setFixedLegendItems(chartLegend);
    }

    private LegendItemCollection createLegendCool() {

        List<T> data = tsc.getData();
        LegendItemCollection legend = new LegendItemCollection();

        TreeMap<Comparable<?>, Integer> counter = this.createCounterFor(this.currentColor, data);

        for (Entry<Comparable<?>, Integer> et : counter.entrySet()) {

            String key =
                    this.currentColor.isNumericValue()
                            ? et.getKey().toString()
                            : (String) et.getKey();

            int value = et.getValue();

            Shape shape = new Rectangle(14, 14);

            Color color =
                    colorManager.getColorFromPattern(this.currentColor, key, highlightSubject);

            legend.add(new LegendItem(key + " (" + value + ")", null, null, key, shape, color));
        }

        return legend;
    }

    protected String[] createYAxisTags() {

        String[] tagArray = new String[tsc.getSeriesCount()];
        for (int i = 0; i < tsc.getRowCount(); i++) {

            tagArray[i] = ((TaskSeriesPlus) tsc.getSeries(i)).getHeaderString();
        }

        return tagArray;
    }

    public void fireChange() {
        this.getChart().fireChartChanged();
    }

    public void highlightSpecificItems(
            Set<Comparable<?>> highlightTopic, Function<T, Comparable<?>> function) {

        this.quickHighlightRenderer.setTopic(highlightTopic, function);
        this.plot.setRenderer(quickHighlightRenderer);
        this.currentColor.setHighlightedNow(true);
        this.highlightSubject = "specificasdf";
    }

    public void stopHighlightingSpecificItems() {

        this.plot.setRenderer(standardRenderer);
        this.currentColor.setHighlightedNow(false);
    }

    public void highlight(String subject) {

        if (this.currentColor != null) {
            this.currentColor.setHighlightedNow(true);
        }

        this.highlightSubject = subject;
    }

    public void returnToColorType() {

        if (currentColor != null) {
            this.currentColor.setHighlightedNow(false);
        }
    }

    public void recolorLegend() {

        Iterator<LegendItem> it = chartLegend.iterator();

        while (it.hasNext()) {

            LegendItem li = it.next();

            String legendItemLabelText = li.getURLText();

            if (currentColor == null) {

                li.setFillPaint(colorManager.getDefaultColor());

            } else {

                li.setFillPaint(
                        colorManager.getColorFromPattern(
                                currentColor, legendItemLabelText, highlightSubject));
            }
        }
    }

    public String toString() {
        return this.name;
    }

    public void setYAxisVisible(boolean setting) {
        plot.getRangeAxis().setVisible(setting);
    }

    public void setLegendVisible(boolean setting) {
        this.getChart().getLegend().setVisible(setting);
    }

    /**
     * @return the tsc
     */
    public TaskSeriesCollectionPlus<T> getTsc() {
        return tsc;
    }

    /**
     * @return the currentColorType
     */
    public ColorOption<T> getCurrentColorType() {
        return this.currentColor;
    }

    @Override
    public void setCurrentColorType(ColorOption<T> colorOption) {

        this.currentColor = colorOption;
    }

    public int getNumberOfLegendItems() {
        return this.getChart().getPlot().getLegendItems().getItemCount();
    }

    public void redraw(TaskSeriesCollectionPlus<T> tscp) {

        tsc = tscp;

        createLegend();

        boolean visible = plot.getRangeAxis().isVisible();

        yAxis = new SymbolAxis(yAxisName, createYAxisTags());
        Font tickLabelFont = new Font("Arial", Font.PLAIN, 10);
        yAxis.setTickLabelFont(tickLabelFont);
        yAxis.setGridBandsVisible(false);
        plot.setRangeAxis(yAxis);

        addHorizontalMarkers();

        ((XYPlot) this.getChart().getPlot()).setDataset(new XYTaskDataset(tsc));
        this.getChart().setTitle(tsc.getTitle());

        this.setYAxisVisible(visible);
    }

    private TreeMap<Comparable<?>, Integer> createCounterFor(ColorOption<T> option, List<T> data) {

        TreeMap<Comparable<?>, Integer> counter = new TreeMap<>();

        data.forEach(eventBlock -> count(counter, eventBlock, option));

        return counter;
    }

    private TreeMap<Comparable<?>, Integer> count(
            TreeMap<Comparable<?>, Integer> counter, T t, ColorOption<T> option) {

        String resultAsString = option.getFunction().apply(t);
        boolean isNumericValue = option.isNumericValue();
        Comparable<?> result = resultAsString;

        if (isNumericValue) {
            result = Integer.parseInt(resultAsString);
        }

        counter.merge(result, 1, Integer::sum);

        return counter;
    }

    private void addHorizontalMarkers() {

        plot.clearRangeMarkers();

        int markerLoc = 0;

        ValueMarker marker = new ValueMarker(markerLoc - 0.5); // position is the value on the axis

        marker.setStroke(gridStroke);
        marker.setPaint(colorManager.getGridColor());

        plot.addRangeMarker(marker);

        for (int i = 0; i < tsc.getRowCount(); i++) {
            markerLoc++;
            if (((TaskSeriesPlus) tsc.getSeries(i)).isHeaderRow()) {
                marker = new ValueMarker(markerLoc - 0.5); // position is the value on the axis

                marker.setStroke(gridStroke);
                marker.setPaint(colorManager.getGridColor());
                plot.addRangeMarker(marker);
            }
        }
    }

    @Override
    public void selectNewColor(ColorOption<T> selectedOption) {

        this.currentColor = selectedOption;

        this.createLegend();
        this.fireChange();
    }

    private class QuickHighlightRenderer extends MyRenderer {

        /** */
        private static final long serialVersionUID = 1L;

        private Set<Comparable<?>> topic;
        private Function<T, Comparable<?>> function;

        public void setTopic(Set<Comparable<?>> topic, Function<T, Comparable<?>> function) {

            this.topic = topic;
            this.function = function;
        }

        @Override
        public Paint getItemPaint(int row, int col) {

            T tsk = (T) tsc.getSeries(row).get(col);

            return topic.contains(this.function.apply(tsk))
                    ? colorManager.getHighlightColor()
                    : colorManager.getLowlightColor();
        }
    }

    private class MyRenderer extends XYBarRenderer {

        /** */
        private static final long serialVersionUID = 1L;

        public MyRenderer() {
            this.setUseYInterval(true);
            this.setBarPainter(new StandardXYBarPainter());
            this.setDrawBarOutline(false);

            this.setShadowXOffset(0);
            this.setShadowYOffset(0);
            this.setDefaultToolTipGenerator(xyToolTipGenerator);
        }

        @Override
        public Paint getItemPaint(int row, int col) {

            if (currentColor == null) {
                return colorManager.getDefaultColor();
            } else {

                @SuppressWarnings("unchecked")
                T tsk = (T) tsc.getSeries(row).get(col);

                Function<T, String> function = currentColor.getFunction();

                return colorManager.getColorFromPattern(
                        currentColor, function.apply(tsk), highlightSubject);
            }
        }
    }
}
