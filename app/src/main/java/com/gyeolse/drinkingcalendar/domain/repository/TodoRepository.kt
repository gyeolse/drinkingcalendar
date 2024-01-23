package com.gyeolse.drinkingcalendar.domain.repository

import com.gyeolse.drinkingcalendar.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun observeTodos(): Flow<List<Todo>>

    suspend fun addTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)
}

/*
* 1. observeTodos : todo 데이터를 관찰하다가 받아올 수 있도록
* */