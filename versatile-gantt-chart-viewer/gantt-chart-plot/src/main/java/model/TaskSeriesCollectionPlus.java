package model;

import org.jfree.data.gantt.TaskSeriesCollection;

import java.util.ArrayList;
import java.util.List;

public class TaskSeriesCollectionPlus<T> extends TaskSeriesCollection {

    /** */
    private static final long serialVersionUID = 1L;
    private List<T> data = new ArrayList<>();
    private String title;

    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> items) {
        this.data = items;
    }
}
