package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status);
    
    List<Task> findByPriorityOrderByCreatedAtDesc(com.taskmanager.model.TaskPriority priority);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate IS NOT NULL AND t.dueDate < :now AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate IS NOT NULL AND t.dueDate BETWEEN :start AND :end")
    List<Task> findTasksDueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    List<Task> findAllByOrderByCreatedAtDesc();
}
