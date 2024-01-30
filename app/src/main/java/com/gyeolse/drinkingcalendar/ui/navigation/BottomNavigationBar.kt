package com.gyeolse.drinkingcalendar.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gyeolse.drinkingcalendar.ui.main.MainScreen
import com.gyeolse.drinkingcalendar.ui.main.MainViewModel
import com.gyeolse.drinkingcalendar.ui.screens.CalendarScreen
import com.gyeolse.drinkingcalendar.ui.screens.ProfileScreen
import com.gyeolse.drinkingcalendar.ui.screens.StatisticsScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(mainScreenViewModel: MainViewModel) {
    // initialize the default selected item.
    var navigationSelectedItem by remember {
        mutableStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavigationItem().bottomNavigationItems().forEachIndexed { _, navigationItem ->
                    NavigationBarItem(
                        selected = navigationItem.route == currentDestination?.route,
                        label = {
                            Text(navigationItem.label)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.label
                            )
                        },
                        onClick = {
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = ScreenInfo.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues)) {
            composable(ScreenInfo.Home.route) {
                CalendarScreen(navController)
//                HomeScreen(navController)
            }
            composable(ScreenInfo.Statistics.route) {
                StatisticsScreen(navController)
            }
            composable(ScreenInfo.Profile.route) {
                ProfileScreen(navController)
            }
            composable(ScreenInfo.Settings.route) {
                MainScreen(viewModel = mainScreenViewModel)
            }
        }
    }

}
