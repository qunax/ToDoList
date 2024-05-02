package com.example.todolist.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ColorsApiResponse(
    val colors : List<String>
)
