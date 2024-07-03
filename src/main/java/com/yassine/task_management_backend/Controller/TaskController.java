package com.yassine.task_management_backend.Controller;


import com.yassine.task_management_backend.Entity.Task;
import com.yassine.task_management_backend.Entity.User;
import com.yassine.task_management_backend.Repository.UserRepository;
import com.yassine.task_management_backend.Service.TaskService;
import com.yassine.task_management_backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Task> getAllTasks(Principal principal) {
        System.out.println(principal.getName());
        User user = userRepository.findByEmail(principal.getName());
        return taskService.getAllTasksByUser(user.getId());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<Task> task = taskService.getTaskByIdAndUser(taskId, user.getId());
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        task.setUser(user);
        return taskService.createTask(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails, Principal principal) {
        System.out.println(taskId);
        System.out.println(taskDetails);
        System.out.println(principal);
        User user = userRepository.findByEmail(principal.getName());
        Optional<Task> task = taskService.getTaskByIdAndUser(taskId, user.getId());
        if (task.isPresent()) {
            Task updatedTask = task.get();
            updatedTask.setTitle(taskDetails.getTitle());
            updatedTask.setDescription(taskDetails.getDescription());
            updatedTask.setDueDate(taskDetails.getDueDate());
            updatedTask.setStatus(taskDetails.getStatus());
            return ResponseEntity.ok(taskService.createTask(updatedTask));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<Task> task = taskService.getTaskByIdAndUser(taskId, user.getId());
        if (task.isPresent()) {
            taskService.deleteTask(taskId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
