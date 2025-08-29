package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @GetMapping("/")
    public String index(Model model) {
        List<Task> allTasks = taskService.getAllTasks();
        List<Task> overdueTasks = taskService.getOverdueTasks();
        
        model.addAttribute("tasks", allTasks);
        model.addAttribute("overdueTasks", overdueTasks);
        model.addAttribute("taskStatuses", TaskStatus.values());
        model.addAttribute("taskPriorities", TaskPriority.values());
        model.addAttribute("newTask", new Task());
        
        return "index";
    }
    
    @PostMapping("/tasks")
    public String createTask(@Valid @ModelAttribute("newTask") Task task, 
                           BindingResult result, 
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please fix the errors below.");
            return "redirect:/";
        }
        
        taskService.createTask(task);
        redirectAttributes.addFlashAttribute("success", "Task created successfully!");
        return "redirect:/";
    }
    
    @PostMapping("/tasks/{id}/update")
    public String updateTask(@PathVariable Long id, 
                           @ModelAttribute Task task, 
                           RedirectAttributes redirectAttributes) {
        Task updatedTask = taskService.updateTask(id, task);
        if (updatedTask != null) {
            redirectAttributes.addFlashAttribute("success", "Task updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Task not found!");
        }
        return "redirect:/";
    }
    
    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Task deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Task not found!");
        }
        return "redirect:/";
    }
    
    @PostMapping("/tasks/{id}/status")
    public String updateTaskStatus(@PathVariable Long id, 
                                 @RequestParam TaskStatus status, 
                                 RedirectAttributes redirectAttributes) {
        Task updatedTask = taskService.updateTaskStatus(id, status);
        if (updatedTask != null) {
            redirectAttributes.addFlashAttribute("success", "Task status updated!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Task not found!");
        }
        return "redirect:/";
    }
    
    @PostMapping("/tasks/{id}/priority")
    public String updateTaskPriority(@PathVariable Long id, 
                                   @RequestParam TaskPriority priority, 
                                   RedirectAttributes redirectAttributes) {
        Task updatedTask = taskService.updateTaskPriority(id, priority);
        if (updatedTask != null) {
            redirectAttributes.addFlashAttribute("success", "Task priority updated!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Task not found!");
        }
        return "redirect:/";
    }
    
    @GetMapping("/tasks/filter")
    public String filterTasks(@RequestParam(required = false) TaskStatus status,
                            @RequestParam(required = false) TaskPriority priority,
                            Model model) {
        List<Task> tasks;
        
        if (status != null) {
            tasks = taskService.getTasksByStatus(status);
        } else if (priority != null) {
            tasks = taskService.getTasksByPriority(priority);
        } else {
            tasks = taskService.getAllTasks();
        }
        
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskStatuses", TaskStatus.values());
        model.addAttribute("taskPriorities", TaskPriority.values());
        model.addAttribute("newTask", new Task());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);
        
        return "index";
    }
}
