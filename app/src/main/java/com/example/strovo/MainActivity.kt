package com.example.strovo

import android.graphics.ColorFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strovo.screen.DashboardScreen
import com.example.strovo.screen.HistoricScreen
import com.example.strovo.screen.ProfileScreen
import com.example.strovo.screen.Screen
import com.example.strovo.ui.theme.StrovoTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.utils.TokenManager
import com.example.strovo.viewmodel.StravaViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                        composable(Screen.Profile.route) {
                            ProfileScreen(navController)
                        }
                        composable(Screen.Historic.route) {
                            HistoricScreen(navController)
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar{
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.outline_archive_icon),
                    contentDescription = "Historique",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = { Text("Historique") },
            selected = false,
            onClick = { navController.navigate(Screen.Historic.route) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.dashboard_svgrepo_com),
                    contentDescription = "Dashboard",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = { Text("Dashboard") },
            selected = false,
            onClick = { navController.navigate(Screen.Dashboard.route) }
        )
    }
}