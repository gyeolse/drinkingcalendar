package com.gyeolse.drinkingcalendar.data.repository

import android.app.Application
import androidx.room.Room
import com.gyeolse.drinkingcalendar.data.data_source.TodoDatabase
import com.gyeolse.drinkingcalendar.domain.model.Todo
import com.gyeolse.drinkingcalendar.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(application: Application) : TodoRepository {

    // application 을 넘겨받아야함.
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java,
        "todo-db"
    ).build()

    override fun observeTodos(): Flow<List<Todo>> {
        return db.todoDao().todos()
    }

    override suspend fun addTodo(todo: Todo) {
        return db.todoDao().insert(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        return db.todoDao().update(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        return db.todoDao().delete(todo)
    }
}

/*
* 1. 실제로 Repository에 접근하도록 로직이 작성된 코드
* 2. Database를 들고 있도록 작성.
* */