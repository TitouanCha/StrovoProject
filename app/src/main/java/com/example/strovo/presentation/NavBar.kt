package com.example.strovo.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
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


@Composable
fun BottomNavBar(
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
) {
    NavigationBar(
        modifier = Modifier.height(100.dp)
    ){
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.dashboard_svgrepo_com),
                    contentDescription = "Dashboard Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentPage == 0,
            onClick = {
                onPageSelected(0)
            },
//            modifier = Modifier
//                .then(
//                if (currentRoute == Screen.Dashboard.route) {
//                    Modifier
//                        .background(MaterialTheme.colorScheme.primary)
//                } else {
//                    Modifier
//                }
//            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.progress_svgrepo_com),
                    contentDescription = "Progress Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentPage == 1,
            onClick = {
                onPageSelected(1)
            },
//            modifier = Modifier
//                .then(
//                    if (currentRoute == Screen.Progress.route) {
//                        Modifier.background(MaterialTheme.colorScheme.primary)
//                    } else {
//                        Modifier
//                    }
//                )

        )
//        NavigationBarItem(
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.time_planning_svgrepo_com),
//                    contentDescription = "Dashboard Icon",
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            },
//            selected = false,
//            onClick = {
//                if (currentRoute != Screen.Map.route) {
//                    navController.navigate(Screen.Map.route)
//                }
//            },
//            modifier = Modifier
//                .padding(end = 16.dp)
//                .then(
//                    if (currentRoute == Screen.Map.route) {
//                        Modifier.background(MaterialTheme.colorScheme.primary)
//                    } else {
//                        Modifier
//                    }
//                )
//        )
    }
}