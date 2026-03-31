package taskengine.taskengine_project_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Entity
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    public ProjectTask() {
    }


    public ProjectTask(Project project, TaskStatus status, Long taskId, Long id) {
        this.project = project;
        this.status = status;
        this.taskId = taskId;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "ProjectTask{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", status=" + status +
                ", project=" + project +
                '}';
    }
}
