package com.mdoc.simpletodo

import com.mdoc.simpletodo.data.TodoEntity

sealed class TodoEvent {
    data class DeleteTodo(val todo: TodoEntity) : TodoEvent()
    data class OnDoneChange(val todo: TodoEntity, val isDone: Boolean) : TodoEvent()
    data class OnTodoClick(val todo: TodoEntity) : TodoEvent()
    object AddNewTodo : TodoEvent()
    object OnUndoDeleteClick : TodoEvent()
}
