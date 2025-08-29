package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private Task testTask2;

    @BeforeEach
    void setUp() {
        testTask = new Task("Test Task", "Test Description");
        testTask.setId(1L);
        testTask.setStatus(TaskStatus.PENDING);
        testTask.setPriority(TaskPriority.MEDIUM);
        testTask.setDueDate(LocalDateTime.now().plusDays(7));

        testTask2 = new Task("Test Task 2", "Test Description 2");
        testTask2.setId(2L);
        testTask2.setStatus(TaskStatus.IN_PROGRESS);
        testTask2.setPriority(TaskPriority.HIGH);
    }

    @Test
    void testGetAllTasks() {
        // Given
        List<Task> expectedTasks = Arrays.asList(testTask, testTask2);
        when(taskRepository.findAllByOrderByCreatedAtDesc()).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = taskService.getAllTasks();

        // Then
        assertEquals(expectedTasks.size(), actualTasks.size());
        assertEquals(expectedTasks, actualTasks);
        verify(taskRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void testGetTaskById_WhenTaskExists() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // When
        Optional<Task> result = taskService.getTaskById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testTask, result.get());
        verify(taskRepository).findById(1L);
    }

    @Test
    void testGetTaskById_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Task> result = taskService.getTaskById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(taskRepository).findById(999L);
    }

    @Test
    void testCreateTask() {
        // Given
        Task newTask = new Task("New Task", "New Description");
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // When
        Task result = taskService.createTask(newTask);

        // Then
        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(taskRepository).save(newTask);
    }

    @Test
    void testUpdateTask_WhenTaskExists() {
        // Given
        Task updateData = new Task("Updated Task", "Updated Description");
        updateData.setStatus(TaskStatus.COMPLETED);
        updateData.setPriority(TaskPriority.URGENT);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task result = taskService.updateTask(1L, updateData);

        // Then
        assertNotNull(result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTask_WhenTaskDoesNotExist() {
        // Given
        Task updateData = new Task("Updated Task", "Updated Description");
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Task result = taskService.updateTask(999L, updateData);

        // Then
        assertNull(result);
        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testDeleteTask_WhenTaskExists() {
        // Given
        when(taskRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = taskService.deleteTask(1L);

        // Then
        assertTrue(result);
        verify(taskRepository).existsById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTask_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = taskService.deleteTask(999L);

        // Then
        assertFalse(result);
        verify(taskRepository).existsById(999L);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void testGetTasksByStatus() {
        // Given
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findByStatusOrderByCreatedAtDesc(TaskStatus.PENDING)).thenReturn(expectedTasks);

        // When
        List<Task> result = taskService.getTasksByStatus(TaskStatus.PENDING);

        // Then
        assertEquals(expectedTasks.size(), result.size());
        assertEquals(expectedTasks, result);
        verify(taskRepository).findByStatusOrderByCreatedAtDesc(TaskStatus.PENDING);
    }

    @Test
    void testGetTasksByPriority() {
        // Given
        List<Task> expectedTasks = Arrays.asList(testTask2);
        when(taskRepository.findByPriorityOrderByCreatedAtDesc(TaskPriority.HIGH)).thenReturn(expectedTasks);

        // When
        List<Task> result = taskService.getTasksByPriority(TaskPriority.HIGH);

        // Then
        assertEquals(expectedTasks.size(), result.size());
        assertEquals(expectedTasks, result);
        verify(taskRepository).findByPriorityOrderByCreatedAtDesc(TaskPriority.HIGH);
    }

    @Test
    void testGetOverdueTasks() {
        // Given
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findOverdueTasks(any(LocalDateTime.class))).thenReturn(expectedTasks);

        // When
        List<Task> result = taskService.getOverdueTasks();

        // Then
        assertEquals(expectedTasks.size(), result.size());
        assertEquals(expectedTasks, result);
        verify(taskRepository).findOverdueTasks(any(LocalDateTime.class));
    }

    @Test
    void testUpdateTaskStatus_WhenTaskExists() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task result = taskService.updateTaskStatus(1L, TaskStatus.COMPLETED);

        // Then
        assertNotNull(result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTaskStatus_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Task result = taskService.updateTaskStatus(999L, TaskStatus.COMPLETED);

        // Then
        assertNull(result);
        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testUpdateTaskPriority_WhenTaskExists() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task result = taskService.updateTaskPriority(1L, TaskPriority.URGENT);

        // Then
        assertNotNull(result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTaskPriority_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Task result = taskService.updateTaskPriority(999L, TaskPriority.URGENT);

        // Then
        assertNull(result);
        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
