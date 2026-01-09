package com.example.strovo.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.utils.TokenManager
import com.example.strovo.viewmodel.StravaViewModel

@Composable
fun DashboardScreen(navController: NavController) {


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
                            text = "Course du 12 Juin 2024 a Yerres",
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
                ){
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Distance",
                            modifier = Modifier,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "15km",
                            modifier = Modifier,
                            fontSize = 20.sp
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Allure",
                            modifier = Modifier,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "5:00 /km",
                            modifier = Modifier,
                            fontSize = 20.sp
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Durée",
                            modifier = Modifier,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "1h15",
                            modifier = Modifier,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}