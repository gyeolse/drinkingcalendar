package com.gyeolse.drinkingcalendar.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gyeolse.drinkingcalendar.domain.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}


/*
    1. Database entites 뒤에 원하는 entity 들을 나열한다.
    2. 숫자를 올리기 -> upgrade에 대한 처리가 가능하다.
    3. todoDao 객체를 가져올 수 있도록 설정한다.
* */