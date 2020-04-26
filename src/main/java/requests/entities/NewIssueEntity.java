package requests.entities;

public class NewIssueEntity extends BaseEntity {
    private Project project = new Project();
    private String summary = "REST API lets you create issues!";
    private String description = "Let's create a new issue using YouTrack's REST API.";

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
