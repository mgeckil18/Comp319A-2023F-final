// MainScreen.kt
package com.example.quizdir

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = { navController.navigate("quiz") },
            modifier = Modifier
                .size(width = 200.dp, height = 50.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(50) // Fully rounded corners for a circular shape
        ) {
            Text(text = "Play")
        }
        Button(
            onClick = { navController.navigate("scoreBoard") },
            modifier = Modifier
                .size(width = 200.dp, height = 50.dp),
            shape = RoundedCornerShape(50) // Fully rounded corners for a circular shape
        ) {
            Text(text = "Go to Scoreboard")
        }
    }
}

