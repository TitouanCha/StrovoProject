package com.example.strovo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strovo.screen.DashboardScreen
import com.example.strovo.screen.ProgressScreen
import com.example.strovo.screen.Screen
import com.example.strovo.ui.theme.StrovoTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.strovo.screen.ActivityDetails
import com.example.strovo.screen.MonthlyActivitiesScreen
import com.example.strovo.screen.SettingsScreen
import com.example.strovo.utils.TokenManager
import com.example.strovo.viewmodel.StravaViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StrovoTheme {
                val navController = rememberNavController()
                val tokenManager = TokenManager(LocalContext.current)
                val stravaViewModel: StravaViewModel = viewModel()
                val context = LocalContext.current

                LaunchedEffect(Unit){
                    if(tokenManager.hasTokens()){
                        stravaViewModel.refreshAccessToken( tokenManager.getRefreshToken()!!, context )
                    }else{
                        Toast.makeText(context, "Veuillez vous connecter à Strava via le profil", Toast.LENGTH_LONG).show()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(navController)
                    }
                )
                { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Dashboard.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Dashboard.route) {
                            DashboardScreen(navController, stravaViewModel)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController)
                        }
                        composable(Screen.Progress.route) {
                            ProgressScreen(navController, stravaViewModel)
                        }
                        composable(
                            route = Screen.MonthlyActivities.route,
                            arguments = listOf(
                                navArgument("monthIndex") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val monthIndex = backStackEntry.arguments?.getInt("monthIndex") ?: 0
                            MonthlyActivitiesScreen(
                                navController = navController,
                                viewModel = stravaViewModel,
                                monthIndex = monthIndex
                            )
                        }
                        composable(
                            route = Screen.ActivityDetails.route,
                            arguments = listOf(
                                navArgument("activityId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
                            ActivityDetails(
                                navController = navController,
                                viewModel = stravaViewModel,
                                activityId = activityId
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun BottomNavBar(navController: NavController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    NavigationBar{
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.progress_svgrepo_com),
                    contentDescription = "Progress Icon",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = { Text("Progrés") },
            selected = currentRoute == Screen.Progress.route,
            onClick = {
                if(currentRoute != Screen.Progress.route) {
                    navController.navigate(Screen.Progress.route)
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.dashboard_svgrepo_com),
                    contentDescription = "Dashboard Icon",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = { Text("Dashboard") },
            selected = currentRoute == Screen.Dashboard.route,
            onClick = {
                if(currentRoute != Screen.Dashboard.route) {
                    navController.navigate(Screen.Dashboard.route)
                }
            }
        )
    }
}