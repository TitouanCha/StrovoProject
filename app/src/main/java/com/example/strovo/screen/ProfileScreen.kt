package com.example.strovo.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.BuildConfig
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.utils.TokenManager
import com.example.strovo.viewmodel.StravaViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: StravaViewModel = viewModel()) {
    var textValue by remember { mutableStateOf("") }
    var tokenManager = TokenManager(LocalContext.current)
    var context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                painter = androidx.compose.ui.res.painterResource(id =
                    if(tokenManager.hasTokens())
                        com.example.strovo.R.drawable.baseline_check_circle
                    else
                        com.example.strovo.R.drawable.outline_dangerous_24),
                contentDescription = "Connection ok Icon",
                modifier = Modifier
                    .size(45.dp)
                    .weight(1f)
            )

        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val clientId = BuildConfig.STRAVA_CLIENT_ID
                val url = "https://www.strava.com/oauth/authorize?client_id=$clientId&response_type=code&redirect_uri=http://localhost/exchange_token&approval_prompt=force&scope=read,activity:read"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Autorisation strava"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = androidx.compose.ui.res.painterResource(id = com.example.strovo.R.drawable.open_outline_svgrepo_com),
                contentDescription = "Strava Icon"
            )
        }
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center

        ) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = {
                    Text(
                        text = "Entrer le code Strava",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 4.dp)
            )
            Button(
                onClick = {
                    viewModel.getAccessToken(textValue, context)
                    navController.navigate(Screen.Dashboard)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "Valider"
                )
            }
        }
    }
}