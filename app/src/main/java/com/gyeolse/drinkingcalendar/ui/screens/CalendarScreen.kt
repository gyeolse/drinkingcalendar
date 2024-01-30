package com.gyeolse.drinkingcalendar.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gyeolse.drinkingcalendar.SimpleCalendarTitle
import com.gyeolse.drinkingcalendar.ui.theme.DrinkingcalendarTheme
import com.gyeolse.drinkingcalendar.util.Day
import com.gyeolse.drinkingcalendar.util.rememberFirstCompletelyVisibleMonth
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(navController: NavController, modifier: Modifier = Modifier) {
    DrinkingcalendarTheme {
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
            Spacer(modifier = Modifier)
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
}