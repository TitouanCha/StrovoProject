package com.example.strovo.presentation.Settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.R
import com.example.strovo.component.HeaderComponent
import com.example.strovo.data.utils.TokenManager

@Composable
fun SettingsScreen(navController: NavController) {
    var textValue by remember { mutableStateOf("") }
    var tokenManager = TokenManager(LocalContext.current)
    var context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderComponent(
            title = "Paramètres", null,
            modifier = Modifier.weight(1f)
        ) { }
        Box(
            modifier = Modifier.weight(9f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Statut de la connexion Strava",
                    fontSize = 18.sp,
                    modifier = Modifier.weight(3f)
                )
                Image(
                    painter = painterResource(
                        id =
                            if (tokenManager.hasTokens())
                                R.drawable.baseline_check_circle
                            else
                                R.drawable.outline_dangerous_24
                    ),
                    contentDescription = "Connection ok Icon",
                    modifier = Modifier
                        .size(45.dp)
                        .weight(1f)
                )

            }
        }
    }
}