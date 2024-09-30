package com.example.lab08.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab08.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
