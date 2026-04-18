package model;

import org.jfree.data.gantt.TaskSeries;

@SuppressWarnings("serial")
public class TaskSeriesPlus extends TaskSeries {

    public static int creationCounter = 0;

    private boolean headerRow;
    private String header;

    public TaskSeriesPlus() {
        super(creationCounter++ + "");
    }

    public String getHeaderString() {
        return this.headerRow ? this.getHeader() : "";
    }

    /**
     * @return the headerRow
     */
    public boolean isHeaderRow() {
        return headerRow;
    }

    /**
     * @param headerRow the headerRow to set
     */
    public void setHeaderRow(boolean headerRow) {
        this.headerRow = headerRow;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
