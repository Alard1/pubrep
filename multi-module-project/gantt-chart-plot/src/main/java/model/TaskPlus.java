package model;

import org.jfree.data.gantt.Task;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class TaskPlus extends Task {

    /** */
    private static final long serialVersionUID = 1L;

    private static int creationCounter = 0;
    protected final LocalDate startDate;
    protected final LocalDate endDate;

    public TaskPlus(LocalDate start, LocalDate end) {
        super(
                creationCounter++ + "",
                Date.from(start.atStartOfDay().toInstant(ZoneOffset.UTC)),
                Date.from(end.atStartOfDay().toInstant(ZoneOffset.UTC)));

        this.startDate = start;
        this.endDate = end;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
