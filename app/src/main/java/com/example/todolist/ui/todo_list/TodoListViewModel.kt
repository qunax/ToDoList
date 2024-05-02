package com.example.todolist.ui.todo_list

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.data_source.ApiService
import com.example.todolist.data.data_source.TodoApi
import com.example.todolist.domain.model.ColorsApiResponse
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.domain.repository.UiConfigRepository
import com.example.todolist.util.Routes
import com.example.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import javax.inject.Inject


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    //private val uiConfigRepository: UiConfigRepository
) : ViewModel() {
    val mutex1 = Mutex()
    val mutex2 = Mutex()
    val mutex3 = Mutex()

    val todos = todoRepository.getAllTodos()

    var backgroundColor = "#ffffff"

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo : Todo? = null


    init {
        viewModelScope.launch {
            backgroundColor = try{
                TodoApi.retrofitService.getRandomColors(2).colors[0]
            } catch (e: IOException){
                "#ffffff"
            } catch (e: HttpException){
                "#ffffff"
            }
            //backgroundColor = uiConfigRepository.getRandomBackgroundColor().colors[0]
        }
    }


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
                            todoRepository.insertTodo(todo)
                        }
                    }
                }
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    mutex2.withLock {
                        deletedTodo = event.todo
                        todoRepository.deleteTodo(event.todo)
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
                        todoRepository.insertTodo(
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