package com.example.tasksync.service;

import com.example.tasksync.model.Task;
import com.example.tasksync.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 🧠 TaskService
 *
 * This service layer handles all CRUD operations (Create, Read, Update, Delete)
 * for Task objects. It acts as the bridge between the Controller and Repository.
 *
 * Responsibilities:
 * ✅ Implements the business logic
 * ✅ Performs validation before saving or updating
 * ✅ Automatically manages ID and timestamp fields
 */
@Service
public class TaskService {

    // Injecting the repository that interacts with the database
    private final TaskRepository repo;

    /**
     * Constructor-based dependency injection.
     * Spring Boot automatically provides an instance of TaskRepository.
     */
    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    /**
     * 📋 Get all tasks from the database.
     *
     * This simply fetches every record from the `tasks` table using JPA.
     *
     * @return List of all Task objects.
     */
    public List<Task> getAll() {
        return repo.findAll(); // Equivalent to SQL: SELECT * FROM tasks
    }

    /**
     * 🔍 Get a single task by its ID.
     *
     * Uses Optional to handle "not found" cases gracefully.
     *
     * @param id Task ID (String)
     * @return Task object if found, or null if not found.
     */
    public Task getById(String id) {
        Optional<Task> o = repo.findById(id); // Tries to find a task by primary key
        return o.orElse(null); // Return the task if found, otherwise null
    }

    /**
     * 🆕 Create a new task in the database.
     *
     * - Generates a unique UUID if the client doesn't provide one.
     * - Sets the `updatedAt` timestamp to the current time.
     * - Saves the task using the repository.
     *
     * @param t Task object (received from the client)
     * @return Saved Task object (with ID and updated timestamp)
     */
    @Transactional // Ensures that all DB operations inside run atomically (all or nothing)
    public Task create(Task t) {
        // ✅ If the client didn't send an ID, generate a random one (UUID)
        if (t.getId() == null || t.getId().isBlank()) {
            t.setId(UUID.randomUUID().toString());
        }

        // ✅ Set the current time as the last updated timestamp
        if (t.getUpdatedAt() == null)
            t.setUpdatedAt(Instant.now());

        // ✅ Save the task in the database and return it
        return repo.save(t);
    }

    /**
     * ✏️ Update an existing task.
     *
     * - Fetches the existing record from DB.
     * - Updates only non-null fields.
     * - Refreshes the `updatedAt` timestamp.
     * - Saves the updated object.
     *
     * @param id Task ID (String)
     * @param t  Updated Task data from client
     * @return Updated Task object
     */
    @Transactional
    public Task update(String id, Task t) {
        // 🔎 Find the task by ID; throw an error if not found
        Task existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));

        // 📝 Update only fields provided in the request
        if (t.getTitle() != null)
            existing.setTitle(t.getTitle());

        // Always overwrite description and completion status
        existing.setDescription(t.getDescription());
        existing.setCompleted(t.isCompleted());

        // ⏰ Update timestamp
        existing.setUpdatedAt(Instant.now());

        // 💾 Save the updated record and return it
        return repo.save(existing);
    }

    /**
     * 🗑️ Delete a task from the database by its ID.
     *
     * @param id Task ID (String)
     */
    @Transactional
    public void delete(String id) {
        // ❌ Remove task from database by primary key
        repo.deleteById(id);
    }
}
