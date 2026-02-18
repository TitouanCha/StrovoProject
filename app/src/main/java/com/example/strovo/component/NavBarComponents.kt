package com.example.strovo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.strovo.R
import com.example.strovo.screen.Screen



@Composable
fun BottomNavBar(navController: NavController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = Modifier.height(100.dp)
    ){
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.progress_svgrepo_com),
                    contentDescription = "Progress Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = false,//currentRoute == Screen.Progress.route,
            onClick = {
                if (currentRoute != Screen.Progress.route) {
                    navController.navigate(Screen.Progress.route){
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = Modifier
                .padding(end = 16.dp)
                .then(
                if (currentRoute == Screen.Progress.route) {
                    Modifier.background(MaterialTheme.colorScheme.primary)
                } else {
                    Modifier
                }
            )

        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.dashboard_svgrepo_com),
                    contentDescription = "Dashboard Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = false, //currentRoute == Screen.Dashboard.route,
            onClick = {
                if (currentRoute != Screen.Dashboard.route) {
                    navController.navigate(Screen.Dashboard.route){
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .then(
                if (currentRoute == Screen.Dashboard.route) {
                    Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp)
                } else {
                    Modifier
                }
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.time_planning_svgrepo_com),
                    contentDescription = "Dashboard Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = false,
            onClick = {
//                if (currentRoute != Screen.Map.route) {
//                    navController.navigate(Screen.Map.route)
//                }
            },
//            modifier = Modifier
//                .padding(end = 16.dp)
//                .then(
//                    if (currentRoute == Screen.Map.route) {
//                        Modifier.background(MaterialTheme.colorScheme.primary)
//                    } else {
//                        Modifier
//                    }
//                )
        )
    }
}