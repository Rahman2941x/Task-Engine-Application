package taskengine.taskengine_project_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskengine.taskengine_project_service.entity.Project;

import java.util.Optional;

@Repository
public interface ProjectRepo extends JpaRepository<Project,Long> {
    Optional<Project> findByProjectName(String projectName);

    Optional<Project> findByProjectManager(String projectManager);

    Boolean existsByProjectNameAndProjectManager(String projectName, String projectManager);
}
