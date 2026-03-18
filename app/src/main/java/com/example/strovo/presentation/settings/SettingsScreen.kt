package com.example.strovo.presentation.settings

import DisciplineFlowRow
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.R
import com.example.strovo.component.HeaderComponent
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val tokenManager = TokenManager(LocalContext.current)

    val settingsUiState = settingsViewModel.settingsUiState.collectAsState().value
    val isDisciplineListUpdated = remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        settingsViewModel.loadSettings()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderComponent(
            title = "Paramètres", R.drawable.baseline_arrow_left_24,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            navController.navigateUp()
        }
        Column(
            modifier = Modifier
                .weight(9f)
                .padding(16.dp)
        ) {
            when(settingsUiState) {
                is SettingsUiState.Loading -> {
                }
                is SettingsUiState.Error -> {
                }
                is SettingsUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(3f)
                            ) {
                                Text(
                                    text = "Profil connecté :",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,

                                    )
                                if (tokenManager.hasTokens()) {
                                    Text(
                                        text = settingsUiState.settingsData.userName,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                            Image(
                                painter = painterResource(
                                    id =
                                        if (settingsUiState.settingsData.userName != "")
                                            R.drawable.baseline_check_circle
                                        else
                                            R.drawable.outline_dangerous_24
                                ),
                                contentDescription = "Connection stats Icon",
                                modifier = Modifier
                                    .size(45.dp)
                                    .weight(1f)
                            )
                        }
                    }
                    DisciplineFlowRow(
                        userDiscipline = settingsUiState.settingsData.selectedDisciplines,
                        disciplines = settingsUiState.settingsData.allDisciplines,
                        isUpdatedDisciplineList = isDisciplineListUpdated.value,
                        modifier =  Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        addDisciplineClick = { newDisciplineList ->
                            if(isDisciplineListUpdated.value){
                                settingsViewModel.addDisciplines(newDisciplineList)
                            }
                            isDisciplineListUpdated.value = !isDisciplineListUpdated.value
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier,
                        onClick = {
                            tokenManager.clearTokens()
                            navController.navigate("strava_auth") {
                                popUpTo("settings_screen") { inclusive = true }
                            }
                        }
                    ) {
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}