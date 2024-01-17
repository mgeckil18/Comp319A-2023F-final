// AppBar.kt
package com.example.quizdir

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun AppBar(navController: NavController, title: String, showBackButton: Boolean) {
    TopAppBar(
        title = { Text(text = title, color = Color.White) },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = if (showBackButton) {
            { IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            } }
        } else {
            null
        }
    )
}
