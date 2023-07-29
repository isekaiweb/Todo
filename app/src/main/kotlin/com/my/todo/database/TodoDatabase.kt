package com.my.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.todo.database.dao.TodoDao
import com.my.todo.database.model.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}