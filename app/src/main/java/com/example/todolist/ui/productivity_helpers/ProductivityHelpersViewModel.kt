package com.example.todolist.ui.productivity_helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


@HiltViewModel
class ProductivityHelpersViewModel() : ViewModel() {
    init{
        viewModelScope.launch {

        }
    }
}