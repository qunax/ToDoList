package com.example.todolist.ui.add_edit_todo

import android.app.Application
import android.content.Context
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todolist.work.ReminderWorker
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


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

    var date by mutableStateOf("")
        private set

    var time by mutableStateOf("")
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
                    date = todoFromDb.date
                    time = todoFromDb.time
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
            is AddEditTodoEvent.OnDateChange -> {
                date = event.date
            }
            is AddEditTodoEvent.OnTimeChange -> {
                time = event.time
            }
            is AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if(title.isBlank()){
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    message = "The title can't be empty"
                                )
                            )
                        }
                        return@launch
                    }

                    repository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            date = date,
                            time = time,
                            isDone = todo?.isDone ?: false,
                            id = todo?.id
                        )
                    )
                    if(date.isNotBlank() && time.isNotBlank()){
                        ScheduleLocalNotification(event.context)
                    }

                    viewModelScope.launch {
                        _uiEvent.send(
                            UiEvent.PopBackStack
                        )
                    }
                }
            }
        }
    }


    fun ScheduleLocalNotification(context: Context) {
        val application = context.applicationContext as Application
        val workManager = WorkManager.getInstance(application)

        val myWorkRequestBuilder = OneTimeWorkRequestBuilder<ReminderWorker>()
        myWorkRequestBuilder.setInputData(
            workDataOf(
                "NAME" to title,
                "MESSAGE" to description
            )
        )
        val durationInSeconds = GetDurationFromNow().seconds
        myWorkRequestBuilder.setInitialDelay(durationInSeconds, TimeUnit.SECONDS)
        workManager.enqueue(myWorkRequestBuilder.build())
    }


    fun GetDurationFromNow() : Duration {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        val timeToNotify = LocalDateTime.parse(date + " " + time, formatter)
        val timeNow = LocalDateTime.now()

        val durationDiff = Duration.between(timeNow, timeToNotify)
        return durationDiff
    }
}