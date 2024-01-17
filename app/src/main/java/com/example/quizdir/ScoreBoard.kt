// ScoreBoard.kt
package com.example.quizdir

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

object ScoreManager {
    var totalQuestionsAnswered: Int = 0
        private set

    var totalCorrectAnswers: Int = 0
        private set

    fun answerQuestion() {
        totalQuestionsAnswered++
    }
    fun correctQuestion() {
        totalCorrectAnswers++
    }

    fun resetScores() {
        totalQuestionsAnswered = 0
        totalCorrectAnswers = 0
    }
}

object ScorePreferences {
    private const val PREFS_NAME = "QuizDirPrefs"
    private const val KEY_TOTAL_ANSWERED = "totalAnswered"
    private const val KEY_TOTAL_CORRECT = "totalCorrect"

    fun saveScore(context: Context, totalAnswered: Int, totalCorrect: Int) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putInt(KEY_TOTAL_ANSWERED, totalAnswered)
            putInt(KEY_TOTAL_CORRECT, totalCorrect)
            apply()
        }
    }

    fun getTotalAnswered(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_TOTAL_ANSWERED, 0)
    }

    fun getTotalCorrect(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_TOTAL_CORRECT, 0)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScoreBoard(navController: NavController) {
    val totalQuestionsAnswered = ScoreManager.totalQuestionsAnswered
    val totalCorrectAnswers = ScoreManager.totalCorrectAnswers
    val percentKnownCorrect = if (totalQuestionsAnswered > 0) {
        (totalCorrectAnswers.toDouble() / totalQuestionsAnswered.toDouble()) * 100
    } else {
        0.0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ScoreBoard") },
                backgroundColor = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Questions Answered: $totalQuestionsAnswered",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Total Correct Answers: $totalCorrectAnswers",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Percent Correct: ${String.format("%.2f", percentKnownCorrect)}%",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
