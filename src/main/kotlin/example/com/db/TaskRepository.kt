package example.com.db

import example.com.db.DatabaseFactory.suspendTransaction
import example.com.models.Priority
import example.com.models.Task
import example.com.models.TaskTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class TaskRepository : ITaskRepository {
    override fun rowToTask(row: ResultRow): Task = Task(
        name = row[TaskTable.name],
        description = row[TaskTable.description],
        priority = Priority.valueOf(row[TaskTable.priority]),
    )

    override suspend fun allTasks(): List<Task> = suspendTransaction {
        TaskTable.selectAll().map(::rowToTask)
    }

    override suspend fun tasksByPriority(priority: Priority): List<Task> = suspendTransaction {
        TaskTable
            .selectAll().where { (TaskTable.priority eq priority.toString()) }
            .map(::rowToTask)
    }

    override suspend fun taskByName(name: String): Task? = suspendTransaction {
        TaskTable
            .selectAll().where { (TaskTable.name eq name) }
            .limit(1)
            .map(::rowToTask)
            .firstOrNull()
    }

    override suspend fun addTask(task: Task): Unit = suspendTransaction {
        TaskTable.insert {
            it[name] = task.name
            it[description] = task.description
            it[priority] = task.priority.toString()
        }
    }

    override suspend fun removeTask(name: String): Boolean = suspendTransaction {
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.name eq name
        }
        rowsDeleted == 1
    }
}