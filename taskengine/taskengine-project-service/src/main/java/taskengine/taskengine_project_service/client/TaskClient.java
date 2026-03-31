package taskengine.taskengine_project_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import taskengine.taskengine_project_service.configuration.Oauth2feignConfig;
import taskengine.taskengine_project_service.dto.ResponseDTO;
import taskengine.taskengine_project_service.dto.ValidAndInValidTask;

import java.util.List;

@FeignClient(
        name = "taskengine-task-service",
        url = "http://localhost:8081",
        configuration = Oauth2feignConfig.class
)
public interface TaskClient {

    @PostMapping("/task/api/v1/project-task-list")
    ResponseDTO<ValidAndInValidTask> projectTaskList(@RequestBody List<Long> ids);

}
