package com.example.tasksync.repository;

import com.example.tasksync.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 🗂️ TaskRepository
 *
 * This interface acts as the Data Access Layer for the Task entity.
 * It extends JpaRepository, which provides ready-to-use CRUD operations.
 *
 * No need to write SQL or implementation manually — Spring Data JPA
 * auto-generates it at runtime.
 */
public interface TaskRepository extends JpaRepository<Task, String> {
    // ✅ JpaRepository already provides methods like:
    // - findAll() → List<Task>
    // - findById(String id) → Optional<Task>
    // - save(Task task) → Task
    // - deleteById(String id)
    // - existsById(String id)
    //
    // You can also define your own custom queries, e.g.:
    // List<Task> findByCompleted(boolean completed);
}
