package com.gyeolse.drinkingcalendar.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gyeolse.drinkingcalendar.data.repository.TodoRepositoryImpl
import com.gyeolse.drinkingcalendar.domain.repository.TodoRepository
import com.gyeolse.drinkingcalendar.ui.main.MainViewModel

class ViewModelFactory(
    private val application: Application,
    private val repository: TodoRepository = TodoRepositoryImpl(application)
) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    // viewModel에 repository를 지정해서 보내야해서 이렇게 factory를 만들어줬다.
    // override 진행
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // MainViewModel 을 만드는 경우에 뭘할지를 지정해준다
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application = application, repository) as T
        }
        // 그 외에는 일반적으로 동작하겠다.
        return super.create(modelClass)
    }
}