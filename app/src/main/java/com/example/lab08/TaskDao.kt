package com.example.lab08

import com.example.lab08.Room.Task
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    // Obtener todas las tareas
    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task>

    // Insertar una nueva tarea
    @Insert
    suspend fun insertTask(task: Task)

    // Marcar una tarea como completada o no completada
    @Update
    suspend fun updateTask(task: Task)

    // Eliminar todas las tareas
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

    // Eliminar una tarea individual
    @Delete
    suspend fun deleteTask(task: Task)

    // Obtener tareas completadas
    @Query("SELECT * FROM tasks WHERE is_completed = 1")
    suspend fun getCompletedTasks(): List<Task>

    // Obtener tareas pendientes
    @Query("SELECT * FROM tasks WHERE is_completed = 0")
    suspend fun getPendingTasks(): List<Task>

    // Buscar tareas por descripción
    @Query("SELECT * FROM tasks WHERE description LIKE '%' || :query || '%'")
    suspend fun searchTasks(query: String): List<Task>

    // Ordenar por nombre
    @Query("SELECT * FROM tasks ORDER BY description ASC")
    suspend fun getTasksSortedByName(): List<Task>

    // Ordenar por fecha de creación (id)
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    suspend fun getTasksSortedByDate(): List<Task>

    // Ordenar por estado (completada o no)
    @Query("SELECT * FROM tasks ORDER BY is_completed ASC")
    suspend fun getTasksSortedByStatus(): List<Task>
}
