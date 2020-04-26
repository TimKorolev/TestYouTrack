package requests.entities;

public class Project extends BaseEntity{
    private String id = System.getProperty("projectId");

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
