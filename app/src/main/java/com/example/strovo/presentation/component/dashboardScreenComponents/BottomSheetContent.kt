package com.example.strovo.presentation.component.dashboardScreenComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.presentation.component.DataActivityDisplay
import com.example.strovo.data.model.toDiscipline
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm
import com.example.strovo.domain.model.ActivityDetailModel
import java.time.format.DateTimeFormatter

@Composable
fun BottomSheetContent(selectedActivities: List<ActivityDetailModel>, onclick: () -> Unit) {

    selectedActivities[0].let { activity ->
        Column(
            verticalArrangement = Arrangement.Top
        ){
            var fontSize = 25.sp
            if(activity.name.length > 30){
                fontSize = 18.sp
            }
            Text(activity.name,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 4.dp),
                fontSize = fontSize,
                lineHeight = fontSize,
                maxLines = 2,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.alarm_svg),
                    contentDescription = "Timer Icon",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = activity.time,
                    fontSize = 22.sp
                )
            }
            if (activity.type == "Run" || activity.type == "TrailRun" || activity.type == "Hike") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 0.dp),
                ) {
                    DataActivityDisplay(
                        "Distance",
                        "${"%.1f".format(activity.distance)}km",
                        fontSize = 20,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp),
                        ).padding(12.dp)

                    )
                    Box(modifier = Modifier.weight(0.1f))
                    DataActivityDisplay(
                        "Allure",
                        activity.avgSpeed,
                        fontSize = 20,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        ).padding(12.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 8.dp),
            ) {
                Text(
                    "Activité du ${activity.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding()
                )
//                Text(
//                    "Type d'activitées : ${activity.type}",
//                    fontSize = 18.sp,
//                    //lineHeight = 16.sp,
//                    modifier = Modifier.padding()
//                )
            }
            //Spacer(modifier = Modifier.weight(1f))
            if(activity.type.toDiscipline() != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        onClick = {
                            onclick()
                        }
                    ) {
                        Text(
                            "Detail de l'activité",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}