package com.example.tasksync.controller;

import com.example.tasksync.model.Task;
import com.example.tasksync.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                     // Marks this class as a REST controller (returns JSON)
@RequestMapping("/api/tasks")       // Base URL for all task-related APIs
public class TaskController {

    private final TaskService svc;  // Used to call task-related logic (CRUD operations)

    // Constructor to inject TaskService
    public TaskController(TaskService svc) { 
        this.svc = svc; 
    }

    @GetMapping                     // GET /api/tasks → get all tasks
    public List<Task> getAll() { 
        return svc.getAll(); 
    }

    @GetMapping("/{id}")            // GET /api/tasks/{id} → get task by ID
    public ResponseEntity<Task> get(@PathVariable String id) {
        Task t = svc.getById(id);   // Find task by ID
        return t == null 
                ? ResponseEntity.notFound().build()   // If not found → return 404
                : ResponseEntity.ok(t);               // If found → return 200 + data
    }

    @PostMapping                    // POST /api/tasks → create new task
    public ResponseEntity<Task> create(@RequestBody Task t) {
        Task created = svc.create(t); // Save new task
        return ResponseEntity.ok(created); // Return created task
    }

    @PutMapping("/{id}")            // PUT /api/tasks/{id} → update existing task
    public ResponseEntity<Task> update(@PathVariable String id, @RequestBody Task t) {
        return ResponseEntity.ok(svc.update(id, t)); // Update and return updated task
    }

    @DeleteMapping("/{id}")         // DELETE /api/tasks/{id} → delete task
    public ResponseEntity<Void> delete(@PathVariable String id) {
        svc.delete(id);             // Delete task by ID
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
