package com.example.todolist.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.util.Routes
import com.example.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject


@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    val mutex1 = Mutex()
    val mutex2 = Mutex()
    val mutex3 = Mutex()
    val todos = repository.getAllTodos()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo : Todo? = null

    fun onEvent(event : TodoListEvent){
        when(event) {
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))

            }
            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        mutex1.withLock{
                            repository.insertTodo(todo)
                        }
                    }
                }
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    mutex2.withLock {
                        deletedTodo = event.todo
                        repository.deleteTodo(event.todo)
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "Todo deleted",
                            action = "Undo"
                        ))
                    }
                }
            }
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    mutex3.withLock {
                        repository.insertTodo(
                            event.todo.copy(
                                isDone = event.isDone
                            )
                        )
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}