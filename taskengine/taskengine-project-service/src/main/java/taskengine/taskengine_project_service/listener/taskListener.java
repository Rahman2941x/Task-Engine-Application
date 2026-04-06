package taskengine.taskengine_project_service.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import taskengine.taskengine_project_service.configuration.RabbitMqPropertiesConfig;
import taskengine.taskengine_project_service.dto.ProjectTaskDTO;
import taskengine.taskengine_project_service.entity.Project;
import taskengine.taskengine_project_service.entity.ProjectTask;
import taskengine.taskengine_project_service.repository.ProjectRepo;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Component
public class taskListener {

    @Autowired
    private RabbitMqPropertiesConfig props;

    @Autowired
    private ProjectRepo repo;


    @RabbitListener(queues ="${RABBITMQ_PROJECT_TASK_QUEUE}")
    @Transactional
    public  void taskStatusConsumer(ProjectTaskDTO taskDTO) {
        if (taskDTO == null) {
            throw new RuntimeException("Empty TaskDTO from Queue");
        }

        System.out.println("Received DTO: " + taskDTO);

        try {
            Long taskId = taskDTO.taskId();
            Project project = repo.findById(taskDTO.projectId())
                    .orElseThrow(() -> new RuntimeException("Project id is not found"));

            ProjectTask projectTask=project.getProjectTask()
                    .stream()
                    .filter(t->t.getTaskId().equals(taskId))
                    .findFirst()
                    .orElseThrow(()-> new RuntimeException("Task not found in project"));

            projectTask.setStatus(taskDTO.status());

            repo.save(project);
        } catch (Exception e){
            e.printStackTrace(); //  IMPORTANT
            throw new RuntimeException("InComplete TaskDTO details from queue", e);
        }
    }


}
