package taskengine.taskengine_project_service.testController;


import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import taskengine.taskengine_project_service.controller.ProjectController;
import taskengine.taskengine_project_service.dto.ResponseDTO;
import taskengine.taskengine_project_service.entity.Project;
import taskengine.taskengine_project_service.entity.ProjectStatus;
import taskengine.taskengine_project_service.service.ProjectService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProjectController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    void testGetProjectById_Active() throws Exception{

        Project project = new Project();
        project.setId(1L);
        project.setActive(false);

        ResponseDTO<?> responseDTO= new ResponseDTO<>(200,project);

        when(projectService.getProjectById(1L)).thenReturn(ResponseEntity.ok(responseDTO));

        mockMvc.perform(get("/project/api/v1/get-project/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.active").value(false));
    }

    @Test
    void testProjectNotFound()throws Exception{
        when(projectService.getProjectById(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/project/api/v1/get-project/id/1"))
                .andExpect(status().isInternalServerError());

    }

}
