package com.example.strovo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.strovo.R
import com.example.strovo.Screen
import java.nio.file.WatchEvent


@Composable
fun BottomNavBar(
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
) {
    NavigationBar(
        modifier = Modifier.height(100.dp),
    ){
        NavigationBarItem(
            icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.medal_1_svg),
                        contentDescription = "Medal Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(26.dp)
                    )
            },
            label = { Text("Palmares", color = MaterialTheme.colorScheme.onSurfaceVariant)},
            modifier = Modifier.weight(1f),
            selected = currentPage == 0,
            onClick = {
                onPageSelected(0)
            },
        )
        NavigationBarItem(
            icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.dashboard_svg),
                        contentDescription = "Dashboard Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(26.dp)
                    )
            },
            label = { Text("Dashboard", color = MaterialTheme.colorScheme.onSurfaceVariant)},
            modifier = Modifier.weight(1f),
            selected = currentPage == 1,
            onClick = {
                onPageSelected(1)
            },
        )
        NavigationBarItem(
            icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.progress_svg),
                        contentDescription = "Progress Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(26.dp)
                    )
            },
            label = { Text("Progrés", color = MaterialTheme.colorScheme.onSurfaceVariant)},
            modifier = Modifier.weight(1f),
            selected = currentPage == 2,
            onClick = {
                onPageSelected(2)
            }
        )
    }
}