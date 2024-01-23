package com.gyeolse.drinkingcalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gyeolse.drinkingcalendar.ui.main.MainScreen
import com.gyeolse.drinkingcalendar.ui.main.MainViewModel
import com.gyeolse.drinkingcalendar.ui.theme.DrinkingcalendarTheme
import com.gyeolse.drinkingcalendar.util.ViewModelFactory
import com.gyeolse.drinkingcalendar.util.rememberFirstCompletelyVisibleMonth
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // viewModel 을 불러와야한다. viewModel을 통해서 불러오면, repository를 지정할 수 없으므로,
            // factory 를 선언해서 불러오도록 한다.
            // val viewModel: MainViewModel = viewModel(). 불러올 수 없음..
            
            val viewModel: MainViewModel = viewModel(
                factory = ViewModelFactory(application),
            )
            
            MainScreen(viewModel = viewModel)

//            DrinkingcalendarTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
////                    Greeting("Android")
//                }
//            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(500) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val daysOfWeek = remember { daysOfWeek() }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var currentDateText by remember { mutableStateOf("") }
    var currentSojuValue by remember { mutableIntStateOf(0) }
    var currentBeerValue by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
    LaunchedEffect(visibleMonth) {
        selection = null
    }

    val openDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SimpleCalendarTitle(
            modifier = Modifier, currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
        )

        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(day, isSelected = selectedDate == day.date) { day ->
                    selectedDate = if (selectedDate == day.date) null else day.date
                    currentDateText = selectedDate.toString()
                    Log.println(Log.ASSERT, "SEGYEOL : ", selectedDate.toString())
                }
            },
        )
        Spacer(modifier = modifier)
        Text(" Current Value : $currentDateText")
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
//            Button(modifier = modifier, onClick = {
//                currentSojuValue = currentSojuValue.plus(1)
//                // Add Toast
//            }) {
//                Text(text = "소주")
//            }
//            Button(modifier = modifier, onClick = {
//                currentBeerValue = currentBeerValue.plus(1)
//            }) {
//                Text(text = "맥주")
//            }
        }
        Text(" Current Soju value : $currentSojuValue , Current Beer Value : $currentBeerValue")
        FloatingActionButton(onClick = {
            openDialog.value = true
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Floating Action Button")
        }

        if (openDialog.value) {
            AlertDialog(onDismissRequest = {
                openDialog.value = false
            }, title = {
                Text(text = "dialog title")
            }, text = {
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Button(modifier = modifier, onClick = {
                        currentSojuValue = currentSojuValue.plus(1)
                        // Add Toast
                    }) {
                        Text(text = "소주")
                    }
                    Button(modifier = modifier, onClick = {
                        currentBeerValue = currentBeerValue.plus(1)
                    }) {
                        Text(text = "맥주")
                    }
                    Text(" Current Soju value : $currentSojuValue , Current Beer Value : $currentBeerValue")

                }
//
//                Text("안주 입력하는 창")
            }, confirmButton = {
                Button(onClick = {
                    openDialog.value = false
                }) {
                    Text(text = "this is confirm button!! ")
                }
            }, dismissButton = {
                Button(onClick = {
                    openDialog.value = false
                }) {
                    Text(text = "this is dismiss button!! ")
                }
            })
        }
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (daysOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = daysOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            )
        }
    }
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = if (isSelected) Color.Green else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DrinkingcalendarTheme {
        Greeting("Android")
    }
}