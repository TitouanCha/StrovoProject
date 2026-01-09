package com.example.strovo.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.strovo.R
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.MainActivity
import com.example.strovo.utils.DataFormattingUtils
import com.example.strovo.utils.TokenManager
import com.example.strovo.viewmodel.StravaViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.isInitialized
import kotlin.math.log

@Composable
fun RowScope.dataDisplay(title: String, data: String) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = title,
            modifier = Modifier,
            fontSize = 10.sp
        )
        Text(
            text = data,
            modifier = Modifier,
            fontSize = 20.sp
        )
    }
}

@Composable
fun DashboardScreen(navController: NavController, viewModel: StravaViewModel = viewModel()) {
    val context = LocalContext.current
    val isInitialized = viewModel.isInitialized.collectAsState()
    val tokenManager = TokenManager(context)
    val dataFormatting = DataFormattingUtils()

    val activities = viewModel.activities.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    val todayDate = Instant.now()

    LaunchedEffect(isInitialized.value) {
        if (isInitialized.value) {
            val afterDate = todayDate.minus(30, ChronoUnit.DAYS).epochSecond.toString()
            viewModel.getActivities(
                context = context,
                perPage = 30,
                page = 1
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text("Profil")
            IconButton(onClick = {
                navController.navigate(Screen.Profile.route)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_account_icon),
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        when {
            isLoading.value -> CircularProgressIndicator()
            errorMessage.value != null -> Text(text = "Error: ${errorMessage.value}")
            activities.value != null -> {
                activities.value?.firstOrNull()?.let { activity ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.running_man_icon),
                                    contentDescription = "Activities Icon",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 8.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Column(
                                    modifier = Modifier
                                ) {
                                    Text(
                                        text = "Dernieres Activités",
                                        modifier = Modifier,
                                        fontSize = 16.sp,
                                        lineHeight = 18.sp
                                    )
                                    Text(
                                        text = "Course du ${
                                            dataFormatting.stravaDateToLocal(
                                                activity.start_date_local
                                            )
                                        } a Yerres",
                                        modifier = Modifier,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp
                                    )
                                }

                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            ) {
                                dataDisplay("Distance", "${"%.1f".format(activity.distance / 1000)}km")
                                dataDisplay("Durée", dataFormatting.secondsToHms(activity.moving_time))
                                dataDisplay("Allure", dataFormatting.speedToPaceMinPerKm(activity.average_speed))
                            }
                        }
                    }
                }
            }
        }
    }
}

