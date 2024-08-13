package example.com.db

import example.com.models.Priority
import example.com.models.Task
import org.jetbrains.exposed.sql.ResultRow

interface ITaskRepository {
    fun rowToTask(row: ResultRow): Task
    suspend fun allTasks(): List<Task>
    suspend fun tasksByPriority(priority: Priority): List<Task>
    suspend fun taskByName(name: String): Task?
    suspend fun addTask(task: Task)
    suspend fun removeTask(name: String): Boolean
}