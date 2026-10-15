package com.example.strovo.presentation.profile

import DisciplineFlowRow
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
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
import com.example.strovo.presentation.component.HeaderComponent
import com.example.strovo.Screen
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.presentation.component.profileScreenComponents.PbComponent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = TokenManager(LocalContext.current)

    val settingsUiState = profileViewModel.profileUiState.collectAsState().value
    val isDisciplineListUpdated = remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderComponent(
            title = "Profile", R.drawable.arrow_back_svg,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            navController.navigateUp()
        }
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            when(settingsUiState) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier)
                }
                is ProfileUiState.Error -> {
                }
                is ProfileUiState.Success -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.cup_svg),
                            contentDescription = "Calendar Icon",
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Records personnelles",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    PbComponent(
                        title = "5km",
                        pb = settingsUiState.settingsData.userPb?.distance5km,
                        onClick = { activityId ->

                        }
                    )
                    PbComponent(
                        title = "10km",
                        pb = settingsUiState.settingsData.userPb?.distance10km,
                        onClick = { activityId ->

                        }
                    )
                    PbComponent(
                        title = "Semi-marathon",
                        pb = settingsUiState.settingsData.userPb?.distanceHalfMarathon,
                        onClick = { activityId ->

                        }
                    )
                    PbComponent(
                        title = "Marathon",
                        pb = settingsUiState.settingsData.userPb?.distanceMarathon,
                        onClick = { activityId ->

                        }
                    )
                    PbComponent(
                        title = "Course la plus longue",
                        pb = settingsUiState.settingsData.userPb?.distanceLongest,
                        onClick = { activityId ->

                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    DisciplineFlowRow(
                        userDiscipline = settingsUiState.settingsData.selectedDisciplines,
                        disciplines = settingsUiState.settingsData.allDisciplines,
                        isUpdatedDisciplineList = isDisciplineListUpdated.value,
                        modifier =  Modifier
                            .fillMaxWidth(),
                        addDisciplineClick = { newDisciplineList ->
                            if(isDisciplineListUpdated.value){
                                profileViewModel.addDisciplines(newDisciplineList)
                            }
                            isDisciplineListUpdated.value = !isDisciplineListUpdated.value
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = 2.dp
                    )
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            modifier = Modifier,
                            onClick = {
                                coroutineScope.launch {
                                    tokenManager.clearTokens()
                                    navController.navigate(Screen.Auth.route) {
                                        popUpTo("settings_screen") { inclusive = true }
                                    }
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
}