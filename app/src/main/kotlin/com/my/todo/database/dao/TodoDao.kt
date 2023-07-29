package com.my.todo.database.dao

import androidx.room.Dao
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


    @Query(value = "SELECT * FROM todo WHERE id =:id")
    fun getTodo(id: Int): Flow<TodoEntity?>

    @Insert
    fun insertAll(vararg todo: TodoEntity)

    @Query(value = "DELETE FROM todo WHERE id=:id")
    suspend fun deleteTodo(id: Int)
}