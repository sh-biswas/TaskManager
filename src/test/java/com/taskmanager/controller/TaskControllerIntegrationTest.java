package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task testTask;
    private List<Task> testTasks;

    @BeforeEach
    void setUp() {
        testTask = new Task("Test Task", "Test Description");
        testTask.setId(1L);
        testTask.setStatus(TaskStatus.PENDING);
        testTask.setPriority(TaskPriority.MEDIUM);
        testTask.setDueDate(LocalDateTime.now().plusDays(7));

        Task testTask2 = new Task("Test Task 2", "Test Description 2");
        testTask2.setId(2L);
        testTask2.setStatus(TaskStatus.IN_PROGRESS);
        testTask2.setPriority(TaskPriority.HIGH);

        testTasks = Arrays.asList(testTask, testTask2);
    }

    @Test
    void testIndexPage_ShouldReturnOk() throws Exception {
        // Given
        when(taskService.getAllTasks()).thenReturn(testTasks);
        when(taskService.getOverdueTasks()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("overdueTasks"))
                .andExpect(model().attributeExists("taskStatuses"))
                .andExpect(model().attributeExists("taskPriorities"))
                .andExpect(model().attributeExists("newTask"));
    }

    @Test
    void testCreateTask_WithValidData_ShouldRedirect() throws Exception {
        // Given
        Task newTask = new Task("New Task", "New Description");
        when(taskService.createTask(any(Task.class))).thenReturn(newTask);

        // When & Then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "New Task")
                .param("description", "New Description")
                .param("priority", "MEDIUM")
                .param("status", "PENDING"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testCreateTask_WithInvalidData_ShouldRedirectWithError() throws Exception {
        // Given
        when(taskService.createTask(any(Task.class))).thenThrow(new RuntimeException("Validation error"));

        // When & Then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "") // Invalid: empty title
                .param("description", "New Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testUpdateTask_WithValidData_ShouldRedirect() throws Exception {
        // Given
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(testTask);

        // When & Then
        mockMvc.perform(post("/tasks/1/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Updated Task")
                .param("description", "Updated Description")
                .param("status", "COMPLETED")
                .param("priority", "HIGH"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testUpdateTask_WhenTaskNotFound_ShouldRedirectWithError() throws Exception {
        // Given
        when(taskService.updateTask(eq(999L), any(Task.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/tasks/999/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Updated Task")
                .param("description", "Updated Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void testDeleteTask_WhenTaskExists_ShouldRedirect() throws Exception {
        // Given
        when(taskService.deleteTask(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/tasks/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testDeleteTask_WhenTaskDoesNotExist_ShouldRedirectWithError() throws Exception {
        // Given
        when(taskService.deleteTask(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/tasks/999/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void testUpdateTaskStatus_ShouldRedirect() throws Exception {
        // Given
        when(taskService.updateTaskStatus(1L, TaskStatus.COMPLETED)).thenReturn(testTask);

        // When & Then
        mockMvc.perform(post("/tasks/1/status")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("status", "COMPLETED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testUpdateTaskPriority_ShouldRedirect() throws Exception {
        // Given
        when(taskService.updateTaskPriority(1L, TaskPriority.URGENT)).thenReturn(testTask);

        // When & Then
        mockMvc.perform(post("/tasks/1/priority")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("priority", "URGENT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testFilterTasks_ByStatus_ShouldReturnFilteredResults() throws Exception {
        // Given
        List<Task> filteredTasks = Arrays.asList(testTask);
        when(taskService.getTasksByStatus(TaskStatus.PENDING)).thenReturn(filteredTasks);

        // When & Then
        mockMvc.perform(get("/tasks/filter")
                .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("tasks", filteredTasks))
                .andExpect(model().attribute("selectedStatus", TaskStatus.PENDING));
    }

    @Test
    void testFilterTasks_ByPriority_ShouldReturnFilteredResults() throws Exception {
        // Given
        List<Task> filteredTasks = Arrays.asList(testTask);
        when(taskService.getTasksByPriority(TaskPriority.HIGH)).thenReturn(filteredTasks);

        // When & Then
        mockMvc.perform(get("/tasks/filter")
                .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("tasks", filteredTasks))
                .andExpect(model().attribute("selectedPriority", TaskPriority.HIGH));
    }

    @Test
    void testFilterTasks_WithoutParameters_ShouldReturnAllTasks() throws Exception {
        // Given
        when(taskService.getAllTasks()).thenReturn(testTasks);

        // When & Then
        mockMvc.perform(get("/tasks/filter"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("tasks", testTasks));
    }
}
