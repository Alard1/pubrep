package buildingblocks;

/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------------
 * XYTaskDataset.java
 * ------------------
 * (C) Copyright 2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 12-Aug-2008 : Version 1, not yet included in JFreeChart (DG);
 *
 */

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.IntervalXYDataset;

import java.util.Date;

/**
 * A dataset implementation that wraps a {@link TaskSeriesCollection} and presents it as an {@link
 * IntervalXYDataset}, allowing a set of tasks to be displayed using an {@link XYBarRenderer}. This
 * is a very specialised dataset implementation---before using it, you should take some time to
 * understand the use-cases that it is designed for. <br>
 * <br>
 * In this implementation, the start and end times of the underlying tasks are returned as
 * x-intervals, and the y-value for all items in a given series is just the series index (that is,
 * an integer). This may seem back-to-front (and the task times could certainly be returned as
 * y-intervals instead), but this allows the dataset to be used in a combined plot along with a
 * regular time series chart (see the xxx demo for an example).
 */
@SuppressWarnings("serial")
public class XYTaskDataset extends AbstractXYDataset
        implements IntervalXYDataset, DatasetChangeListener {

    /** The underlying tasks. */
    private TaskSeriesCollection underlying;

    /** The y-interval width. */
    private double yIntervalWidth;

    /**
     * Creates a new dataset based on the supplied collection of tasks.
     *
     * @param tasks the underlying dataset (<code>null</code> not permitted).
     */
    public XYTaskDataset(TaskSeriesCollection tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Null 'tasks' argument.");
        }
        this.underlying = tasks;
        this.yIntervalWidth = 0.8;
        this.underlying.addChangeListener(this);
    }

    /**
     * Returns the number of series in the dataset.
     *
     * @return The series count.
     */
    public int getSeriesCount() {
        return this.underlying.getSeriesCount();
    }

    /**
     * Returns the name of a series.
     *
     * @param series the series index (zero-based).
     * @return The name of a series.
     */
    public Comparable<?> getSeriesKey(int series) {
        return this.underlying.getSeriesKey(series);
    }

    /**
     * Returns the number of items (tasks) in the specified series.
     *
     * @param series the series index (zero-based).
     * @return The item count.
     */
    public int getItemCount(int series) {
        return this.underlying.getSeries(series).getItemCount();
    }

    /**
     * Returns the x-value for the specified series. This is a millisecond value in the middle of
     * the specified item (task) within the requested series.
     *
     * @param series the series index.
     * @param item the item index.
     * @return The x-value (in milliseconds).
     */
    public Number getX(int series, int item) {
        TaskSeries s = this.underlying.getSeries(series);
        Task t = s.get(item);
        TimePeriod duration = t.getDuration();
        Date start = duration.getStart();
        Date end = duration.getEnd();
        long mid = (start.getTime() / 2L) + (end.getTime() / 2L);
        return mid;
    }

    /**
     * Returns the y-value for the specified series/item. In this implementation, we return the
     * series index as the y-value (this means that every item in the series has a constant integer
     * value).
     *
     * @param series the series index.
     * @param item the item index.
     * @return The y-value.
     */
    public Number getY(int series, int item) {
        return series;
    }

    /**
     * Returns the starting date/time for the specified item (task) in the given series, measured in
     * milliseconds since 1-Jan-1970 (as in java.util.Date).
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The start date/time.
     */
    public Number getStartX(int series, int item) {
        return getStartXValue(series, item);
    }

    /**
     * Returns the starting date/time for the specified item (task) in the given series, measured in
     * milliseconds since 1-Jan-1970 (as in java.util.Date).
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The start date/time.
     */
    public double getStartXValue(int series, int item) {
        TaskSeries s = this.underlying.getSeries(series);
        Task t = s.get(item);
        TimePeriod duration = t.getDuration();
        Date start = duration.getStart();
        return start.getTime();
    }

    /**
     * Returns the ending date/time for the specified item (task) in the given series, measured in
     * milliseconds since 1-Jan-1970 (as in java.util.Date).
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The end date/time.
     */
    public Number getEndX(int series, int item) {
        return getEndXValue(series, item);
    }

    /**
     * Returns the ending date/time for the specified item (task) in the given series, measured in
     * milliseconds since 1-Jan-1970 (as in java.util.Date).
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The end date/time.
     */
    public double getEndXValue(int series, int item) {
        TaskSeries s = this.underlying.getSeries(series);
        Task t = s.get(item);
        TimePeriod duration = t.getDuration();
        Date end = duration.getEnd();
        return end.getTime();
    }

    /**
     * Returns the starting value of the y-interval for an item in the given series.
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The y-interval start.
     */
    public Number getStartY(int series, int item) {
        return series - this.yIntervalWidth / 2.0;
    }

    /**
     * Returns the starting value of the y-interval for an item in the given series.
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The y-interval start.
     */
    public double getStartYValue(int series, int item) {
        return series - this.yIntervalWidth / 2.0;
    }

    /**
     * Returns the ending value of the y-interval for an item in the given series.
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The y-interval end.
     */
    public Number getEndY(int series, int item) {
        return series + this.yIntervalWidth / 2.0;
    }

    /**
     * Returns the ending value of the y-interval for an item in the given series.
     *
     * @param series the series index.
     * @param item the item (or task) index.
     * @return The y-interval end.
     */
    public double getEndYValue(int series, int item) {
        return series + this.yIntervalWidth / 2.0;
    }

    /**
     * Receives a change event from the underlying dataset and responds by firing a change event for
     * this dataset.
     *
     * @param event the event.
     */
    public void datasetChanged(DatasetChangeEvent event) {
        fireDatasetChanged();
    }
}
