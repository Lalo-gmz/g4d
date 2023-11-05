package mx.lania.g4d.service.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class Issue {

    private long id;
    private long iid;
    private long projectID;
    private String title;
    private String description;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Object closedAt;
    private Object closedBy;
    private List<String> labels;
    private Milestone milestone;
    private List<Object> assignees;
    private Author author;
    private String type;
    private Object assignee;
    private long userNotesCount;
    private long mergeRequestsCount;
    private long upvotes;
    private long downvotes;
    private Object dueDate;
    private boolean confidential;
    private Boolean discussionLocked;
    private String issueType;
    private String webURL;
    private TimeStats timeStats;
    private TaskCompletionStatus taskCompletionStatus;
    private long blockingIssuesCount;
    private boolean hasTasks;
    private String taskStatus;
    private Links links;
    private References references;
    private String severity;
    private Object movedToID;
    private Object serviceDeskReplyTo;

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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(LocalDateTime value) {
        this.createdAt = value;
    }

    @JsonProperty("updated_at")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(LocalDateTime value) {
        this.updatedAt = value;
    }

    @JsonProperty("closed_at")
    public Object getClosedAt() {
        return closedAt;
    }

    @JsonProperty("closed_at")
    public void setClosedAt(Object value) {
        this.closedAt = value;
    }

    @JsonProperty("closed_by")
    public Object getClosedBy() {
        return closedBy;
    }

    @JsonProperty("closed_by")
    public void setClosedBy(Object value) {
        this.closedBy = value;
    }

    @JsonProperty("labels")
    public List<String> getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(List<String> value) {
        this.labels = value;
    }

    @JsonProperty("milestone")
    public Milestone getMilestone() {
        return milestone;
    }

    @JsonProperty("milestone")
    public void setMilestone(Milestone value) {
        this.milestone = value;
    }

    @JsonProperty("assignees")
    public List<Object> getAssignees() {
        return assignees;
    }

    @JsonProperty("assignees")
    public void setAssignees(List<Object> value) {
        this.assignees = value;
    }

    @JsonProperty("author")
    public Author getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(Author value) {
        this.author = value;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value) {
        this.type = value;
    }

    @JsonProperty("assignee")
    public Object getAssignee() {
        return assignee;
    }

    @JsonProperty("assignee")
    public void setAssignee(Object value) {
        this.assignee = value;
    }

    @JsonProperty("user_notes_count")
    public long getUserNotesCount() {
        return userNotesCount;
    }

    @JsonProperty("user_notes_count")
    public void setUserNotesCount(long value) {
        this.userNotesCount = value;
    }

    @JsonProperty("merge_requests_count")
    public long getMergeRequestsCount() {
        return mergeRequestsCount;
    }

    @JsonProperty("merge_requests_count")
    public void setMergeRequestsCount(long value) {
        this.mergeRequestsCount = value;
    }

    @JsonProperty("upvotes")
    public long getUpvotes() {
        return upvotes;
    }

    @JsonProperty("upvotes")
    public void setUpvotes(long value) {
        this.upvotes = value;
    }

    @JsonProperty("downvotes")
    public long getDownvotes() {
        return downvotes;
    }

    @JsonProperty("downvotes")
    public void setDownvotes(long value) {
        this.downvotes = value;
    }

    @JsonProperty("due_date")
    public Object getDueDate() {
        return dueDate;
    }

    @JsonProperty("due_date")
    public void setDueDate(Object value) {
        this.dueDate = value;
    }

    @JsonProperty("confidential")
    public boolean getConfidential() {
        return confidential;
    }

    @JsonProperty("confidential")
    public void setConfidential(boolean value) {
        this.confidential = value;
    }

    @JsonProperty("discussion_locked")
    public Boolean getDiscussionLocked() {
        return discussionLocked;
    }

    @JsonProperty("discussion_locked")
    public void setDiscussionLocked(Boolean value) {
        this.discussionLocked = value;
    }

    @JsonProperty("issue_type")
    public String getIssueType() {
        return issueType;
    }

    @JsonProperty("issue_type")
    public void setIssueType(String value) {
        this.issueType = value;
    }

    @JsonProperty("web_url")
    public String getWebURL() {
        return webURL;
    }

    @JsonProperty("web_url")
    public void setWebURL(String value) {
        this.webURL = value;
    }

    @JsonProperty("time_stats")
    public TimeStats getTimeStats() {
        return timeStats;
    }

    @JsonProperty("time_stats")
    public void setTimeStats(TimeStats value) {
        this.timeStats = value;
    }

    @JsonProperty("task_completion_status")
    public TaskCompletionStatus getTaskCompletionStatus() {
        return taskCompletionStatus;
    }

    @JsonProperty("task_completion_status")
    public void setTaskCompletionStatus(TaskCompletionStatus value) {
        this.taskCompletionStatus = value;
    }

    @JsonProperty("blocking_issues_count")
    public long getBlockingIssuesCount() {
        return blockingIssuesCount;
    }

    @JsonProperty("blocking_issues_count")
    public void setBlockingIssuesCount(long value) {
        this.blockingIssuesCount = value;
    }

    @JsonProperty("has_tasks")
    public boolean getHasTasks() {
        return hasTasks;
    }

    @JsonProperty("has_tasks")
    public void setHasTasks(boolean value) {
        this.hasTasks = value;
    }

    @JsonProperty("task_status")
    public String getTaskStatus() {
        return taskStatus;
    }

    @JsonProperty("task_status")
    public void setTaskStatus(String value) {
        this.taskStatus = value;
    }

    @JsonProperty("_links")
    public Links getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links value) {
        this.links = value;
    }

    @JsonProperty("references")
    public References getReferences() {
        return references;
    }

    @JsonProperty("references")
    public void setReferences(References value) {
        this.references = value;
    }

    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    @JsonProperty("severity")
    public void setSeverity(String value) {
        this.severity = value;
    }

    @JsonProperty("moved_to_id")
    public Object getMovedToID() {
        return movedToID;
    }

    @JsonProperty("moved_to_id")
    public void setMovedToID(Object value) {
        this.movedToID = value;
    }

    @JsonProperty("service_desk_reply_to")
    public Object getServiceDeskReplyTo() {
        return serviceDeskReplyTo;
    }

    @JsonProperty("service_desk_reply_to")
    public void setServiceDeskReplyTo(Object value) {
        this.serviceDeskReplyTo = value;
    }
}
