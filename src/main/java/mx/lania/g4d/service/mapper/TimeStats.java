package mx.lania.g4d.service.mapper;

import com.fasterxml.jackson.annotation.*;

public class TimeStats {

    private long timeEstimate;
    private long totalTimeSpent;
    private Object humanTimeEstimate;
    private Object humanTotalTimeSpent;

    @JsonProperty("time_estimate")
    public long getTimeEstimate() {
        return timeEstimate;
    }

    @JsonProperty("time_estimate")
    public void setTimeEstimate(long value) {
        this.timeEstimate = value;
    }

    @JsonProperty("total_time_spent")
    public long getTotalTimeSpent() {
        return totalTimeSpent;
    }

    @JsonProperty("total_time_spent")
    public void setTotalTimeSpent(long value) {
        this.totalTimeSpent = value;
    }

    @JsonProperty("human_time_estimate")
    public Object getHumanTimeEstimate() {
        return humanTimeEstimate;
    }

    @JsonProperty("human_time_estimate")
    public void setHumanTimeEstimate(Object value) {
        this.humanTimeEstimate = value;
    }

    @JsonProperty("human_total_time_spent")
    public Object getHumanTotalTimeSpent() {
        return humanTotalTimeSpent;
    }

    @JsonProperty("human_total_time_spent")
    public void setHumanTotalTimeSpent(Object value) {
        this.humanTotalTimeSpent = value;
    }
}
