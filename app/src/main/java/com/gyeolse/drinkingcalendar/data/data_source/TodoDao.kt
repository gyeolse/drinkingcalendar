package com.gyeolse.drinkingcalendar.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gyeolse.drinkingcalendar.domain.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY date DESC")
    fun todos(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)
}

/*
    기본적인 기능을 interface에 추가한다.
    flow는 비동기적인 data를 처리하기에 적합하다. coroutines 안에 있는 Flow.
    Entity 이름으로 table을 생성하게 된다. todo 데이터를 얻기 위해서 todos 함수의 query 에서처럼 조회한다.

* */