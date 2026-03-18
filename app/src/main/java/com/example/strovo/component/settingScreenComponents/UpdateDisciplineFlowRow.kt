package com.example.strovo.component.settingScreenComponents

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableInferredTarget
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.data.model.Discipline

@Composable
fun UpdateDisciplineFlowRow(
    userDiscipline: List<Discipline>,
    disciplines: List<Discipline>,
    onAddClick: (Discipline) -> Unit = {},
    onDeleteClick: (Discipline) -> Unit = {}
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        disciplines.forEach { discipline ->
            Card(
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = discipline.name,
                        fontSize = 15.sp,
                        modifier = Modifier
                    )
                    Icon(
                        painter = painterResource(
                            id = if (userDiscipline.contains(discipline))
                                R.drawable.cross_svgrepo_com
                            else
                                R.drawable.baseline_add_24
                        ),
                        modifier = Modifier
                            .padding(0.dp)
                            .clickable{
                                if(userDiscipline.contains(discipline)){
                                    onDeleteClick(discipline)
                                } else {
                                    onAddClick(discipline)
                                }
                            },
                        contentDescription = "Settings Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}