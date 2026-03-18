package com.example.strovo.component.settingScreenComponents

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.data.model.Discipline

@Composable
fun UserDisciplineFlowRow(disciplines: List<Discipline>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        disciplines.forEach { discipline ->
            Card(
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 6.dp)
            ) {
                Text(
                    text = discipline.name,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}