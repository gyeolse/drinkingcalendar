package com.gyeolse.drinkingcalendar.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Todo(
    val title: String,
    val date: Long = Calendar.getInstance().timeInMillis,
    val isDone: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

/* *
    1. Entity class 는 id가 필요하다.
    2. 정적인 id를 만들기 위해서 class 내부에서 선언. autoGenerate true로 set.
 */