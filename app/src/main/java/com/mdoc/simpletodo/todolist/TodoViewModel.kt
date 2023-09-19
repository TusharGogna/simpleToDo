package com.mdoc.simpletodo.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdoc.simpletodo.TodoEvent
import com.mdoc.simpletodo.data.TodoEntity
import com.mdoc.simpletodo.data.TodoRepository
import com.mdoc.simpletodo.utils.Routes
import com.mdoc.simpletodo.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {
    val allTodo = repository.getAllTodo()
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodoTemp: TodoEntity? = null

    fun onEvent(event: TodoEvent) {
        when (event) {
            is TodoEvent.AddNewTodo -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }

            is TodoEvent.DeleteTodo -> {
                viewModelScope.launch {
                    deletedTodoTemp = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            "ToDo Item: ${event.todo.title} deleted successfully.",
                            "Undo"
                        )
                    )
                }
            }

            is TodoEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }

            is TodoEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoID=${event.todo.id}"))
            }

            is TodoEvent.OnUndoDeleteClick -> {
                deletedTodoTemp?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}