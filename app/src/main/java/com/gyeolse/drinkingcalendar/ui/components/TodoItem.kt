package com.gyeolse.drinkingcalendar.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gyeolse.drinkingcalendar.domain.model.Todo
import com.gyeolse.drinkingcalendar.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TodoItem(
    todo: Todo,
    // todo 를 return 하도록
    onClick: (todo: Int) -> Unit = {},
    onDeleteClick: (todo: Int) -> Unit = {},
) {
    // Time Formatter
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // click delete를 눌렀을 때에 대한 이벤트
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_delete_24),
            contentDescription = null,
            tint = Color(0xFFA51212),
            modifier = Modifier
                .padding(8.dp)
                .clickable { onDeleteClick(todo.uid) }
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = format.format(Date(todo.date)),
                color = if (todo.isDone) Color.Gray else Color.Black,
                style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None)
            )
            Text(
                text = todo.title,
                color = if (todo.isDone) Color.Gray else Color.Black,
                style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None)
            )

            if (todo.isDone) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = null,
                    tint = Color(0xFF00BCD4),
                )
            }
        }
    }
}