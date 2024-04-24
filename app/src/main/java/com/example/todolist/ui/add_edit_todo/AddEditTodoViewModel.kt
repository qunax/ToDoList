package com.example.todolist.ui.add_edit_todo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
//import androidx.lifecycle.viewmodel.compose.viewModel


@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()



    init {
        val todoId = savedStateHandle.get<Int>("todoId")
        if(todoId != -1){
            viewModelScope.launch {
                repository.getTodoById(todoId!!)?.let{todoFromDb ->
                    title = todoFromDb.title
                    description = todoFromDb.description ?: ""
                    todo  = todoFromDb
                }
            }
        }
    }



    fun onEvent(event: AddEditTodoEvent){
        when(event){
            is AddEditTodoEvent.OnTitleChange -> {
                title   = event.title
            }
            is AddEditTodoEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if(title.isNotBlank()){
                        repository.insertTodo(
                            Todo(
                                title = title,
                                description = description,
                                isDone = todo?.isDone ?: false,
                                id = todo?.id
                            )
                        )
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.PopBackStack
                            )
                        }
                    } else {
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    message = "The title can't be empty"
                                )
                            )
                        }
                        return@launch
                    }
                }
            }
        }
    }
}