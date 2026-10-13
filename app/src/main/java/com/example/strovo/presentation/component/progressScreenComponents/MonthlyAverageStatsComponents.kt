package com.example.strovo.presentation.component.progressScreenComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.presentation.component.DataActivityDisplay
import com.example.strovo.model.strava.AverageStatsModel

@Composable
fun MonthlyAverageStatsComponents(stats: AverageStatsModel?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
        ){
            DataActivityDisplay("Distance Total", stats?.distance ?: "",
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp),
                ).padding(12.dp)
            )
            Box(modifier = Modifier.weight(0.05f))
            DataActivityDisplay("Activités totale", stats?.activities ?: "",
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp),
                ).padding(12.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            DataActivityDisplay("Moyenne hebdomadaire", stats?.weekly_average ?: "",
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp),
                ).padding(12.dp)
            )
            Box(modifier = Modifier.weight(0.05f))
            DataActivityDisplay("Moyenne Mensuelle", stats?.monthly_average ?: "",
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp),
                ).padding(12.dp)
            )
        }
    }
}