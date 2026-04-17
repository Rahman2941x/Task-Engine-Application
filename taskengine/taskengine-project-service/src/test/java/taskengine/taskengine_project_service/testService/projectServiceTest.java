package taskengine.taskengine_project_service.testService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import taskengine.taskengine_project_service.dto.ResponseDTO;
import taskengine.taskengine_project_service.entity.Project;
import taskengine.taskengine_project_service.repository.ProjectRepo;
import taskengine.taskengine_project_service.service.ProjectService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class projectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepo projectRepo;

    @Test
    public  void getProjectByIdReturnProject(){

        Long projectId= 12L;

        Project project= new Project();
        project.setId(projectId);
        project.setActive(true);

        when(projectRepo.findById(12l)).thenReturn(Optional.of(project));

        ResponseEntity<ResponseDTO<?>> projectTestData=projectService.getProjectById(projectId);
        assertNotNull(projectTestData.getBody());
        assertEquals(200,projectTestData.getBody().status());
    }

    @Test
    public  void testGetProjectById_InactiveProject(){

        Long projectId= 12L;

        Project project= new Project();
        project.setId(projectId);
        project.setActive(false);

        when(projectRepo.findById(12l)).thenReturn(Optional.of(project));

        ResponseEntity<ResponseDTO<?>> projectTestData=projectService.getProjectById(projectId);
        assertNotNull(projectTestData.getBody());
        assertEquals(200,projectTestData.getBody().status());
        assertNotNull(projectTestData.getBody().data());

        assertTrue(projectTestData.getBody().data().toString().contains("project is not Active Project id"));
    }


    @Test
    public  void testGetProjectById_NotFound(){

        when(projectRepo.findById(1l)).thenReturn(Optional.empty());

        RuntimeException exception =assertThrows(RuntimeException.class,()->{
            projectService.getProjectById(1l);
        });
        assertTrue(exception.toString().contains("Project id not found::"+1L));
    }

}
