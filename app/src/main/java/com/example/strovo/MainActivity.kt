package com.example.strovo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strovo.ui.theme.StrovoTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.strovo.data.local.StrovoDataBase
import com.example.strovo.data.repository.AuthRepositoryImpl
import com.example.strovo.data.repository.DashboardRepositoryImpl
import com.example.strovo.data.repository.HallOfFameRepositoryImpl
import com.example.strovo.data.repository.ProfileRepositoryImpl
import com.example.strovo.data.repository.ProgressRepositoryImpl
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.FirstLaunchManager
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.repository.DashboardRepository
import com.example.strovo.domain.repository.ProgressRepository
import com.example.strovo.presentation.component.BottomNavBar
import com.example.strovo.presentation.component.MainPagerScreen
import com.example.strovo.presentation.map.ActivitiesMapScreen
import com.example.strovo.presentation.activityDetails.ActivityDetails
import com.example.strovo.presentation.monthlyActivities.MonthlyActivitiesScreen
import com.example.strovo.presentation.profile.ProfileScreen
import com.example.strovo.presentation.dashboard.DashboardViewModel
import com.example.strovo.presentation.progress.ProgressViewModel
import com.example.strovo.presentation.profile.ProfileViewModel
import com.example.strovo.presentation.auth.AuthScreen
import com.example.strovo.presentation.auth.AuthViewModel
import com.example.strovo.presentation.hallOfFame.AddCompScreen
import com.example.strovo.presentation.hallOfFame.HallOfFameScreen
import com.example.strovo.presentation.hallOfFame.HallOfFameViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var appViewModels: AppViewModel

    private fun injectDependencies(tokenManager: TokenManager, disciplineManager: DisciplineManager, firstLaunchManager: FirstLaunchManager): AppViewModel {
        val database = Room.databaseBuilder(
            applicationContext,
            StrovoDataBase::class.java,
            "strovo_database"
        ).build()
        val raceDao = database.compDao()

        val authRepository = AuthRepositoryImpl(tokenManager)
        val dashboardRepository = DashboardRepositoryImpl(tokenManager)
        val progressRepository = ProgressRepositoryImpl(tokenManager, disciplineManager)
        val profileRepository = ProfileRepositoryImpl(disciplineManager, tokenManager)
        val hallOfFameRepository = HallOfFameRepositoryImpl(raceDao)
        
        return AppViewModel(
            authViewModel = AuthViewModel(tokenManager, disciplineManager, firstLaunchManager, authRepository),
            dashboardViewModel = DashboardViewModel(tokenManager, disciplineManager, dashboardRepository, authRepository),
            progressViewModel = ProgressViewModel(progressRepository),
            profileViewModel = ProfileViewModel(profileRepository),
            hallOfFameViewModel = HallOfFameViewModel(hallOfFameRepository)
        )
    }

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        val tokenManager = TokenManager(this)
        val disciplineManager = DisciplineManager(this)
        val firstLaunchManager = FirstLaunchManager(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appViewModels = injectDependencies(tokenManager, disciplineManager, firstLaunchManager)

        setContent {
            StrovoTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = remember(navBackStackEntry) {
                    navBackStackEntry?.destination?.route
                }
                AppNavigation(context, appViewModels, currentRoute.toString(), navController)
            }
        }
    }
}