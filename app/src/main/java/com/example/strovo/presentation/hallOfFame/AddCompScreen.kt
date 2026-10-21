package com.example.strovo.presentation.hallOfFame

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.strovo.R
import com.example.strovo.presentation.component.HeaderComponent

@Composable
fun AddCompScreen(navController: NavController, viewModel: HallOfFameViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            HeaderComponent(
                title = "Ajouter une course",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                icon = R.drawable.arrow_back_svg,
            ) { }
        }
    }
}