package com.example.tasksync.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * 🧩 Task Entity
 *
 * Represents a task in the database. Each instance of this class
 * corresponds to one record in the `tasks` table.
 */
@Entity                     // Marks this class as a JPA entity (database-mapped object)
@Table(name = "tasks")      // Maps this entity to the "tasks" table
public class Task {

    /**
     * 🔑 Primary Key — unique identifier for each task.
     * Using String here allows flexible IDs (e.g., UUIDs).
     */
    @Id
    private String id;

    /** 🏷️ The short title or name of the task. */
    private String title;

    /** 📝 A longer text describing the task in detail. */
    private String description;

    /** ✅ Indicates if the task is completed or still pending. */
    private boolean completed;

    /**
     * 🕒 Timestamp representing when the task was last updated.
     * Useful for sync/conflict resolution and sorting by recency.
     */
    private Instant updatedAt;

    // 🔧 Default constructor (required by JPA)
    public Task() {}

    // 🧱 Parameterized constructor for easy object creation
    public Task(String id, String title, String description, boolean completed, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.updatedAt = updatedAt;
    }

    // ==========================
    // 🧩 Getters and Setters
    // ==========================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // 🧾 Optional — useful for debugging/logging
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
