package com.example.tasksync.controller;

import com.example.tasksync.model.Task;
import com.example.tasksync.service.SyncService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController                     // This class handles REST API requests (returns JSON)
@RequestMapping("/api/sync")        // Base URL for all sync APIs
public class SyncController {

    private final SyncService svc;  // Used to call the sync logic

    // Constructor to inject SyncService
    public SyncController(SyncService svc) { 
        this.svc = svc; 
    }

    @PostMapping                    // Runs when client sends POST request to /api/sync
    public SyncService.SyncResult sync(@RequestBody List<Task> tasks) {
        // Call the service to process and sync the task list
        return svc.sync(tasks);
    }
}
