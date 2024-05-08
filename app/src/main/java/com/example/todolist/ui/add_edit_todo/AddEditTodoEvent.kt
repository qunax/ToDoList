package com.example.todolist.ui.add_edit_todo

import android.content.Context


sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String) : AddEditTodoEvent()
    data class OnDescriptionChange(val description: String) : AddEditTodoEvent()
    data class OnDateChange(val date: String) : AddEditTodoEvent()
    data class OnTimeChange(val time: String) : AddEditTodoEvent()
    data class OnSaveTodoClick(val context: Context) : AddEditTodoEvent()
}