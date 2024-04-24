package com.example.todolist.domain.repository

import com.example.todolist.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun getTodoById(id: Int) : Todo?

    fun getAllTodos() : Flow<List<Todo>>
}