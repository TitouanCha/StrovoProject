package com.example.strovo.component

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.component.settingScreenComponents.UpdateDisciplineFlowRow
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.utils.DisciplineManager

@Composable
fun OnboardingComponent(
    context: Context,
    onClick: (List<Discipline>) -> Unit = { }
) {
    val disciplineList = remember { mutableStateOf<List<Discipline>>(listOf()) }
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenue sur Strovo !",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Avant de commencer, sélectionnez les disciplines que vous souhaitez suivre",
                fontSize = 20.sp,
                lineHeight = 24.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Vous pourrez les modifier plus tard dans les paramètres",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            UpdateDisciplineFlowRow(
                userDiscipline = disciplineList.value,
                disciplines = Discipline.entries,
                onAddClick = { discipline ->
                    disciplineList.value = disciplineList.value + discipline
                },
                onDeleteClick = { discipline ->
                    disciplineList.value = disciplineList.value - discipline
                },
                cardColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onClick(disciplineList.value)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Valider",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}