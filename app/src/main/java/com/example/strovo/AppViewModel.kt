package com.example.strovo

import com.example.strovo.presentation.auth.AuthViewModel
import com.example.strovo.presentation.dashboard.DashboardViewModel
import com.example.strovo.presentation.hallOfFame.HallOfFameViewModel
import com.example.strovo.presentation.profile.ProfileViewModel
import com.example.strovo.presentation.progress.ProgressViewModel

data class AppViewModel (
    val authViewModel: AuthViewModel,
    val dashboardViewModel: DashboardViewModel,
    val progressViewModel: ProgressViewModel,
    val profileViewModel: ProfileViewModel,
    val hallOfFameViewModel: HallOfFameViewModel
)