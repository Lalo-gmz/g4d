package mx.lania.g4d.service.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Links {

    private String self;
    private String notes;
    private String awardEmoji;
    private String project;
    private Object closedAsDuplicateOf;

    @JsonProperty("self")
    public String getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(String value) {
        this.self = value;
    }

    @JsonProperty("notes")
    public String getNotes() {
        return notes;
    }

    @JsonProperty("notes")
    public void setNotes(String value) {
        this.notes = value;
    }

    @JsonProperty("award_emoji")
    public String getAwardEmoji() {
        return awardEmoji;
    }

    @JsonProperty("award_emoji")
    public void setAwardEmoji(String value) {
        this.awardEmoji = value;
    }

    @JsonProperty("project")
    public String getProject() {
        return project;
    }

    @JsonProperty("project")
    public void setProject(String value) {
        this.project = value;
    }

    @JsonProperty("closed_as_duplicate_of")
    public Object getClosedAsDuplicateOf() {
        return closedAsDuplicateOf;
    }

    @JsonProperty("closed_as_duplicate_of")
    public void setClosedAsDuplicateOf(Object value) {
        this.closedAsDuplicateOf = value;
    }
}
