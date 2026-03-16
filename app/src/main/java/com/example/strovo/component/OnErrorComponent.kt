package com.example.strovo.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnErrorComponent(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Erreur lors du chargement des activités",
                modifier = Modifier.padding(8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Verrifier votre connexion a Strava.",
                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp, start = 8.dp, end = 8.dp),
                fontSize = 16.sp
            )
            Text(
                text = errorMessage,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                fontSize = 16.sp
            )
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                onClick = {
                    onRetry()
                }
            ) {
                Text(
                    text = "Actualiser les données",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}