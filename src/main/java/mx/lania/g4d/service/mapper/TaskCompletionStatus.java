package mx.lania.g4d.service.mapper;

import com.fasterxml.jackson.annotation.*;

public class TaskCompletionStatus {

    private long count;
    private long completedCount;

    @JsonProperty("count")
    public long getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(long value) {
        this.count = value;
    }

    @JsonProperty("completed_count")
    public long getCompletedCount() {
        return completedCount;
    }

    @JsonProperty("completed_count")
    public void setCompletedCount(long value) {
        this.completedCount = value;
    }
}
