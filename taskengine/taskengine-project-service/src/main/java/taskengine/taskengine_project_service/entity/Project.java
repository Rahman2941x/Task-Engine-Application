package taskengine.taskengine_project_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import javax.naming.Name;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;
    private String projectName;
    private String projectManager;
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @ElementCollection
    @CollectionTable(
            name = "project_members",
            joinColumns =@JoinColumn(name = "project_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"user_id","project_id"}
            )
    )
    @Column(name = "user_id")
    private List<Long> projectMemberId;

    @OneToMany(mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<ProjectTask> projectTask;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime projectDeadLine;


    @PrePersist
    public void prePersist(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.isActive=true;
        this.projectStatus=ProjectStatus.CREATED;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt=LocalDateTime.now();
    }

    public Project() {
    }

    public Project(Long id, String projectName, String projectManager, ProjectStatus projectStatus, List<Long> projectMemberId, List<ProjectTask> projectTask, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime projectDeadLine) {
        this.id = id;
        this.projectName = projectName;
        this.projectManager = projectManager;
        this.projectStatus = projectStatus;
        this.projectMemberId = projectMemberId;
        this.projectTask = projectTask;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projectDeadLine = projectDeadLine;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public LocalDateTime getProjectDeadLine() {
        return projectDeadLine;
    }

    public void setProjectDeadLine(LocalDateTime projectDeadLine) {
        this.projectDeadLine = projectDeadLine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<Long> getProjectMemberId() {
        return projectMemberId;
    }

    public void setProjectMemberId(List<Long> projectMemberId) {
        this.projectMemberId = projectMemberId;
    }

    public List<ProjectTask> getProjectTask() {
        return projectTask;
    }

    public void setProjectTask(List<ProjectTask> projectTask) {
        this.projectTask = projectTask;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", projectManager='" + projectManager + '\'' +
                ", projectStatus=" + projectStatus +
                ", projectMemberId=" + projectMemberId +
                ", projectTask=" + projectTask +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", projectDeadLine=" + projectDeadLine +
                '}';
    }
}


