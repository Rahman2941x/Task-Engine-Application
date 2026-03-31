package com.taskengine.taskengine_task_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    @Column(name = "task_name")
    private String taskName;
    @Column(name = "task_description",nullable = false)
    private String taskDescription;
    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @Column(name = "project_id")
    private Long projectId; // if taskType=PROJECT else null
    @Column(name = "assignee",nullable = true)
    private String assignedUser;
    @Column(name = "created_by")
    private String createdBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_status",nullable = false)
    private TaskStatus status;
    @Column(name = "user_accepted")
    private Boolean userAccepted; // if taskType=PROJECT else null
    @Column(name = "dead_line")
    private LocalDateTime dueDate;


    @Column(name = "estimated_hours")
    private Integer estimatedHours;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "task_order")
    private Integer taskOrder;

    @ElementCollection
    @CollectionTable(name = "task_dependencies",joinColumns =@JoinColumn(name = "task_id"))
    @Column(name = "depends_on_task")
    private List<Long> dependencies;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isActive;
    private String remark;
    private LocalDateTime endDate;


    @PrePersist
    public void prePersist(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.isActive=true;
        this.status=TaskStatus.CREATED;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt=LocalDateTime.now();
    }


    public Task() {
    }

    public Task(Long id, String taskName, String taskDescription, TaskType taskType, Long projectId, String assignedUser, String createdBy, TaskStatus status, Boolean userAccepted, LocalDateTime dueDate, Integer estimatedHours, TaskPriority priority, Integer taskOrder, List<Long> dependencies, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt, Boolean isActive, String remark, LocalDateTime endDate) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskType = taskType;
        this.projectId = projectId;
        this.assignedUser = assignedUser;
        this.createdBy = createdBy;
        this.status = status;
        this.userAccepted = userAccepted;
        this.dueDate = dueDate;
        this.estimatedHours = estimatedHours;
        this.priority = priority;
        this.taskOrder = taskOrder;
        this.dependencies = dependencies;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.remark = remark;
        this.endDate = endDate;
    }

    public Integer getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public List<Long> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Long> dependencies) {
        this.dependencies = dependencies;
        this.status=TaskStatus.BLOCKED;
    }

    public Integer getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(Integer taskOrder) {
        this.taskOrder = taskOrder;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return taskDescription;
    }

    public void setDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getName() {
        return taskName;
    }

    public void setName(String name) {
        this.taskName = name;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }


    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(Boolean userAccepted) {
        this.userAccepted = userAccepted;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;

        switch (status){
            case CANCELLED,COMPLETED ->this.endDate=LocalDateTime.now();
            case REOPENED -> this.endDate=null;
        }
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskType=" + taskType +
                ", projectId=" + projectId +
                ", assignedUser='" + assignedUser + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", status=" + status +
                ", userAccepted=" + userAccepted +
                ", dueDate=" + dueDate +
                ", estimatedHours=" + estimatedHours +
                ", priority=" + priority +
                ", taskOrder=" + taskOrder +
                ", dependencies=" + dependencies +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                ", isActive=" + isActive +
                ", remark='" + remark + '\'' +
                ", endDate=" + endDate +
                '}';
    }
}
