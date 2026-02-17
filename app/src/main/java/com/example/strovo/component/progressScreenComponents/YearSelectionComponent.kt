package com.example.strovo.component.progressScreenComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import java.time.LocalDate

@Composable
fun YearSelectionComponent(
    incrementYear: () -> Unit,
    decrementYear: () -> Unit,
    selectedYear: Int
){
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                decrementYear()
            },
            modifier = Modifier
                .weight(1f),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                contentDescription = "Previous Year",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "Donnée de $selectedYear",
            modifier = Modifier
                .weight(3f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if(selectedYear != LocalDate.now().year){
            IconButton(
                onClick = {
                    incrementYear()
                },
                modifier = Modifier
                    .weight(1f),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                    contentDescription = "Previous Year",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }else {
            Box(modifier = Modifier.weight(1f)) {}
        }
    }
}