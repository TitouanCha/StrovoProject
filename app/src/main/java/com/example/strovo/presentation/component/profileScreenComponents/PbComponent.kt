package com.example.strovo.presentation.component.profileScreenComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.domain.model.AthletePB
import com.example.strovo.domain.model.BestChrono

@Composable
fun PbComponent(
    title: String,
    pb: BestChrono?,
    onClick: (activityId: String) -> Unit,
) {
    OutlinedCard(
        modifier = Modifier.padding(vertical = 4.dp),
        onClick = { onClick(pb?.activityId ?: "") }
    ){
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (pb == null) {
                    Text(
                        text = "Aucun PB enregistré",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    Text(
                        text = pb.chrono.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = pb.speed.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = pb.date.toString(),
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}