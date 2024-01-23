package com.gyeolse.drinkingcalendar.ui.main

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gyeolse.drinkingcalendar.domain.model.Todo
import com.gyeolse.drinkingcalendar.domain.repository.TodoRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application, private val todoRepository: TodoRepository) :
    AndroidViewModel(application) {
    private val _items = mutableStateOf(emptyList<Todo>())
    val items: State<List<Todo>> = _items

    private var recentlyDeleteTodo: Todo? = null
    init {
        viewModelScope.launch {
            todoRepository.observeTodos()
                .collect { todos ->
                    _items.value = todos
                }
        }
    }
    fun addTodo(text: String) {
        viewModelScope.launch {
            todoRepository.addTodo(Todo(title = text))
        }
    }

    fun toggle(index: Int) {
        val todo = _items.value.find { todo -> todo.uid == index }

        // 만약 todo가 있다면. nullable 체크 ?
        todo?.let {
            viewModelScope.launch {
                todoRepository.updateTodo(it.copy(isDone = !todo.isDone).apply {
                    this.uid = it.uid
                })
            }
        }
    }

    fun delete(index: Int) {
        val todo = _items.value.find { todo -> todo.uid == index }
        todo?.let {
            viewModelScope.launch {
                todoRepository.deleteTodo(it)
                recentlyDeleteTodo = it
            }
        }
    }

    fun restoreTodo() {
        viewModelScope.launch {
            // recentlyDeleteTodo 가 nullable이라서 이걸 처리해줘야함.
            // elvis 연산자 사용 return@launch 라고 하면 viewModelScope에 있었던 @launch가 취소된다.
            todoRepository.addTodo(recentlyDeleteTodo ?: return@launch)
            recentlyDeleteTodo = null
        }
    }
}

/*
* 1.state 를 가져서 viewModel이 자동으로 업데이트 되도록 설정한다.
* 2. addTodo 를 실행시키기 위해서는 coroutine을 만들어줘야함. (suspend fun)이기 때문.
* */