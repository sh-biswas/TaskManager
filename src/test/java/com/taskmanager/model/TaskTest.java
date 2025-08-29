package com.taskmanager.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Validator validator;
    private Task task;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.MEDIUM);
    }

    @Test
    void testValidTask() {
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertTrue(violations.isEmpty(), "Valid task should have no validation violations");
    }

    @Test
    void testTaskWithNullTitle() {
        task.setTitle(null);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "Task with null title should have validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void testTaskWithEmptyTitle() {
        task.setTitle("");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "Task with empty title should have validation violations");
    }

    @Test
    void testTaskWithBlankTitle() {
        task.setTitle("   ");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "Task with blank title should have validation violations");
    }

    @Test
    void testTaskWithLongTitle() {
        task.setTitle("a".repeat(256));
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "Task with title longer than 255 characters should have validation violations");
    }

    @Test
    void testTaskWithLongDescription() {
        task.setDescription("a".repeat(1001));
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "Task with description longer than 1000 characters should have validation violations");
    }

    @Test
    void testTaskConstructor() {
        Task newTask = new Task("Constructor Task", "Constructor Description");
        assertEquals("Constructor Task", newTask.getTitle());
        assertEquals("Constructor Description", newTask.getDescription());
        assertEquals(TaskStatus.PENDING, newTask.getStatus());
        assertEquals(TaskPriority.MEDIUM, newTask.getPriority());
        assertNotNull(newTask.getCreatedAt());
        assertNotNull(newTask.getUpdatedAt());
    }

    @Test
    void testTaskSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = now.plusDays(7);

        task.setId(1L);
        task.setDueDate(dueDate);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        assertEquals(1L, task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertEquals(TaskPriority.MEDIUM, task.getPriority());
        assertEquals(dueDate, task.getDueDate());
        assertEquals(now, task.getCreatedAt());
        assertEquals(now, task.getUpdatedAt());
    }

    @Test
    void testTaskStatusValues() {
        assertEquals(4, TaskStatus.values().length);
        assertEquals("Pending", TaskStatus.PENDING.getDisplayName());
        assertEquals("In Progress", TaskStatus.IN_PROGRESS.getDisplayName());
        assertEquals("Completed", TaskStatus.COMPLETED.getDisplayName());
        assertEquals("Cancelled", TaskStatus.CANCELLED.getDisplayName());
    }

    @Test
    void testTaskPriorityValues() {
        assertEquals(4, TaskPriority.values().length);
        assertEquals("Low", TaskPriority.LOW.getDisplayName());
        assertEquals("Medium", TaskPriority.MEDIUM.getDisplayName());
        assertEquals("High", TaskPriority.HIGH.getDisplayName());
        assertEquals("Urgent", TaskPriority.URGENT.getDisplayName());
    }
}
