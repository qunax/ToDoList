package com.example.todolist.ui.todo_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.util.UiEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
){
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    //val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember {SnackbarHostState()}
    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{event ->
            when(event){
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if(result == SnackbarResult.ActionPerformed){
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
                               FloatingActionButton(onClick = {
                                   viewModel.onEvent(TodoListEvent.OnAddTodoClick)
                               }){
                                   Icon(
                                       imageVector = Icons.Default.Add,
                                       contentDescription = "Add"
                                   )
                               }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ){
            items(todos.value){todo -> 
                TodoItem(
                    todo = todo
                    , onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}