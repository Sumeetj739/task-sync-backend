package com.example.tasksync.service;

import com.example.tasksync.model.Task;
import com.example.tasksync.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

/**
 * 🔄 SyncService
 *
 * This service handles synchronization between the client-side (offline/local)
 * and the server-side (database). It ensures that:
 * - Tasks are created, updated, or merged properly.
 * - Conflicts are detected when both client and server have newer versions.
 * - Errors are collected instead of crashing the sync process.
 */
@Service
public class SyncService {

    private final TaskRepository repo;

    // Dependency Injection for TaskRepository
    public SyncService(TaskRepository repo) {
        this.repo = repo;
    }

    /**
     * 📦 Inner class to represent the result of the sync operation.
     * It contains:
     * - synced: list of successfully updated/created tasks
     * - conflicts: list of conflicts (client vs server versions)
     * - errors: list of error messages during sync
     */
    public static class SyncResult {
        public List<Task> synced = new ArrayList<>();
        public List<Map<String, Object>> conflicts = new ArrayList<>();
        public List<String> errors = new ArrayList<>();
    }

    /**
     * ⚙️ sync()
     *
     * Main method that processes synchronization logic.
     *
     * @param clientTasks → The list of tasks sent from the client (may include new, updated, or outdated data)
     * @return SyncResult → Summary of what was synced, conflicted, or failed
     */
    @Transactional // ensures all database changes in this method are atomic
    public SyncResult sync(List<Task> clientTasks) {
        SyncResult result = new SyncResult();

        // Loop through each client task to compare with server data
        for (Task client : clientTasks) {
            try {
                // 🆕 Case 1: Client task has no ID → create new on server
                if (client.getId() == null || client.getId().isBlank()) {
                    client.setId(UUID.randomUUID().toString());
                    client.setUpdatedAt(Instant.now());
                    repo.save(client);
                    result.synced.add(client);
                    continue;
                }

                // 🔍 Case 2: Task ID exists → check if it exists in DB
                Optional<Task> serverOpt = repo.findById(client.getId());

                if (serverOpt.isEmpty()) {
                    // Server doesn’t have this task → create it
                    if (client.getUpdatedAt() == null) client.setUpdatedAt(Instant.now());
                    repo.save(client);
                    result.synced.add(client);
                } else {
                    Task server = serverOpt.get();

                    // Handle timestamps to detect which side has newer data
                    Instant clientTime = client.getUpdatedAt() == null ? Instant.EPOCH : client.getUpdatedAt();
                    Instant serverTime = server.getUpdatedAt() == null ? Instant.EPOCH : server.getUpdatedAt();

                    // ⏫ Case 3a: Client is newer → update server
                    if (clientTime.isAfter(serverTime)) {
                        server.setTitle(client.getTitle());
                        server.setDescription(client.getDescription());
                        server.setCompleted(client.isCompleted());
                        server.setUpdatedAt(client.getUpdatedAt() == null ? Instant.now() : client.getUpdatedAt());
                        repo.save(server);
                        result.synced.add(server);

                    // ⚔️ Case 3b: Server is newer → record a conflict
                    } else if (clientTime.isBefore(serverTime)) {
                        Map<String, Object> conflict = new HashMap<>();
                        conflict.put("id", server.getId());
                        conflict.put("server", server);
                        conflict.put("client", client);
                        result.conflicts.add(conflict);

                    // ⚖️ Case 3c: Timestamps equal → no update needed
                    } else {
                        result.synced.add(server);
                    }
                }
            } catch (Exception ex) {
                // 🛑 Case 4: Catch any exception and continue syncing other tasks
                result.errors.add("Failed to sync task " + client.getId() + ": " + ex.getMessage());
            }
        }

        // ✅ Return summary result to the controller
        return result;
    }
}
