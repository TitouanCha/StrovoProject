package com.example.strovo.presentation.auth

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.strovo.component.Screen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.strovo.component.OnboardingComponent
import com.example.strovo.data.utils.FirstLaunchManager

@Composable
fun AuthScreen(viewModel: AuthViewModel, navController: NavController ) {
    var context = LocalContext.current
    val authUiState by viewModel.authUiState.collectAsState()
    var athleteId by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }

    val isFirstLaunch = remember { mutableStateOf<Boolean>(FirstLaunchManager(context).isFirstLaunch()) }

    LaunchedEffect(Unit) {
        viewModel.refreshStravaToken()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (authUiState) {
                is AuthUiState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.MainPager.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
                is AuthUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Chargement…", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                is AuthUiState.Initial -> {
                    Text(
                        text = "Bienvenue sur Strovo !",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pour commencer, renseignez votre Athlete-ID et votre clé API afin de permettre la récupération des données via Intervals.icu",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val url = "https://intervals.icu/settings"
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Accéder à votre compte Intervals.icu",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = com.example.strovo.R.drawable.open_outline_svgrepo_com),
                            contentDescription = "Open Intervals.icu",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = athleteId,
                        onValueChange = { athleteId = it },
                        label = { Text(text = "Athlete-ID") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text(text = "Clé API") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val isFormValid = athleteId.isNotBlank() && apiKey.isNotBlank()

                    Button(
                        onClick = {
                            if (!isFormValid) {
                                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.saveUserInfo(athleteId, apiKey)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = isFormValid
                    ) {
                        Text(
                            text = "Valider",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                is AuthUiState.Error -> {
                    Toast.makeText(context, (authUiState as AuthUiState.Error).message, Toast.LENGTH_LONG).show()
                    viewModel.resetUiState()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
//    if(isFirstLaunch.value) {
//        OnboardingComponent(
//            context,
//            onClick = { disciplines ->
//                 viewModel.saveUserDisciplines(disciplines)
//                isFirstLaunch.value = false
//            }
//        )
//    }
}