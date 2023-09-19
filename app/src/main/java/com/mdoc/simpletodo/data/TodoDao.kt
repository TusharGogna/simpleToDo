package com.mdoc.simpletodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoEntity: TodoEntity)

    @Delete
    suspend fun deleteTodo(todoEntity: TodoEntity)

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?

    @Query("SELECT * FROM todo")
    fun getAllTodo(): Flow<List<TodoEntity>>
}