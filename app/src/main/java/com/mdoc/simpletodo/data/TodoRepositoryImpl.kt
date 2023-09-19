package com.mdoc.simpletodo.data

import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {

    override suspend fun insertTodo(todoEntity: TodoEntity) {
        dao.insertTodo(todoEntity)
    }

    override suspend fun deleteTodo(todoEntity: TodoEntity) {
        dao.deleteTodo(todoEntity)
    }

    override suspend fun getTodoById(id: Int): TodoEntity? {
        return dao.getTodoById(id)
    }

    override fun getAllTodo(): Flow<List<TodoEntity>> {
        return dao.getAllTodo()
    }
}