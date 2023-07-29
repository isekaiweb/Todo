package com.my.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.my.todo.database.model.TodoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [TodoEntity] access
 **/
@Dao
interface TodoDao {

    @Query(value = "SELECT * FROM todo ORDER BY dueDate Desc")
    fun getTodos(): Flow<List<TodoEntity>>

    @Upsert
    suspend fun insertOrReplaceTodo(todo: TodoEntity)

    @Insert
    fun insertAll(vararg todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)
}