package mx.lania.g4d.service.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class Milestone {

    private long id;
    private long iid;
    private long projectID;
    private String title;
    private String description;
    private String state;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Object dueDate;
    private Object startDate;
    private boolean expired;
    private String webURL;

    @JsonProperty("id")
    public long getID() {
        return id;
    }

    @JsonProperty("id")
    public void setID(long value) {
        this.id = value;
    }

    @JsonProperty("iid")
    public long getIid() {
        return iid;
    }

    @JsonProperty("iid")
    public void setIid(long value) {
        this.iid = value;
    }

    @JsonProperty("project_id")
    public long getProjectID() {
        return projectID;
    }

    @JsonProperty("project_id")
    public void setProjectID(long value) {
        this.projectID = value;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String value) {
        this.title = value;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String value) {
        this.description = value;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String value) {
        this.state = value;
    }

    @JsonProperty("created_at")
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(OffsetDateTime value) {
        this.createdAt = value;
    }

    @JsonProperty("updated_at")
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(OffsetDateTime value) {
        this.updatedAt = value;
    }

    @JsonProperty("due_date")
    public Object getDueDate() {
        return dueDate;
    }

    @JsonProperty("due_date")
    public void setDueDate(Object value) {
        this.dueDate = value;
    }

    @JsonProperty("start_date")
    public Object getStartDate() {
        return startDate;
    }

    @JsonProperty("start_date")
    public void setStartDate(Object value) {
        this.startDate = value;
    }

    @JsonProperty("expired")
    public boolean getExpired() {
        return expired;
    }

    @JsonProperty("expired")
    public void setExpired(boolean value) {
        this.expired = value;
    }

    @JsonProperty("web_url")
    public String getWebURL() {
        return webURL;
    }

    @JsonProperty("web_url")
    public void setWebURL(String value) {
        this.webURL = value;
    }
}
