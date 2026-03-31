package taskengine.taskengine_project_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import taskengine.taskengine_project_service.configuration.Oauth2feignConfig;
import taskengine.taskengine_project_service.dto.IsValidUser;
import taskengine.taskengine_project_service.dto.ResponseDTO;

import java.util.List;

@FeignClient(
        name = "taskengine-user-service",
        url = "http://localhost:8080",
        configuration = Oauth2feignConfig.class
)
public interface UserClient {

    @PostMapping("/user/api/v1/validate-user")
    ResponseDTO<List<IsValidUser>> validateUser(
            @RequestBody List<Long> ids);
}
