package com.example.strovo.presentation.hallOfFame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.R
import com.example.strovo.Screen
import com.example.strovo.presentation.component.HeaderComponent

private data class MockPost(
    val title: String,
    val distanceKm: Double,
    val color: Color
)

private val mockPosts = listOf(
    MockPost("Sortie matin", 10.2, Color(0xFFEF5350)),
    MockPost("Fractionné", 6.5, Color(0xFFAB47BC)),
    MockPost("Trail", 18.4, Color(0xFF42A5F5)),
    MockPost("Footing", 8.0, Color(0xFF26A69A)),
    MockPost("Sortie longue", 21.1, Color(0xFFFFA726)),
    MockPost("Récup", 5.0, Color(0xFF66BB6A)),
    MockPost("Côtes", 7.3, Color(0xFF8D6E63)),
    MockPost("Tempo", 12.0, Color(0xFF5C6BC0)),
    MockPost("Nocturne", 9.4, Color(0xFF29B6F6))
)

@Composable
fun HallOfFameScreen(navController: NavController, viewModel: HallOfFameViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            HeaderComponent(
                title = "Hall of Fame",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                icon = R.drawable.add_svg,
            ) {
                navController.navigate(Screen.AddCompetition)
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(mockPosts) { post ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(post.color),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                post.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text("${post.distanceKm} km", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

