package com.nacho.gamesapplication.presentation.homeScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nacho.gamesapplication.ui.theme.montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier,
                text = "Games Application",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = montserrat,
                    fontSize = 27.sp,
                ),
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    onClick.invoke()
                },
            value = "",
            onValueChange = {
                onClick.invoke()
            },
            enabled = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            },
            placeholder = {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = montserrat,
                    ),
                )
            },
            shape = RoundedCornerShape(100),
        )
    }
}