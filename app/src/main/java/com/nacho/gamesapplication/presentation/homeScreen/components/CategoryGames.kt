package com.nacho.gamesapplication.presentation.homeScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.ui.theme.montserrat

@Composable
fun CategoryGames(
    games: List<Games>,
    title: String,
    onGameClicked: (Int) -> Unit,
    onAllClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                fontFamily = montserrat,
            ),
        )
        Text(
            modifier = Modifier.clickable { onAllClicked.invoke() },
            text = "View all",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                fontFamily = montserrat,
            ),
        )
    }
    GamesRow(
        games = games,
        onGameClicked = onGameClicked,
    )
}