package com.example.quizdir

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter


@Composable
fun QuizScreen(navController: NavController) {

    var currentIndex by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }
    var wrongAnswers by remember { mutableStateOf(0) }
    var showCorrectAnswer by remember { mutableStateOf(false) }
    var triggerNextQuestion by remember { mutableStateOf(false) }
    val context = LocalContext.current

    fun loadDirectorsFromCsv(context: Context): List<String> {
        val directors = mutableSetOf<String>() // Use a set to avoid duplicates

        val inputStream = context.resources.openRawResource(R.raw.movies) // Adjust the file name accordingly
        inputStream.bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line -> // Skip the header row
                val tokens = line.split(",")
                if (tokens.size > 6) {
                    val director = tokens[6].trim().removeSurrounding("\"") // Remove surrounding quotes if present
                    directors.add(director)
                }
            }
        }

        return directors.toList() // Convert to list if you need an ordered collection
    }



    fun generateDirectorOptions(correctDirector: String, directors: List<String>): List<String> {
        val options = mutableSetOf(correctDirector)
        while (options.size < 4) {
            options.add(directors.random())
        }
        return options.shuffled()
    }


    fun loadMoviesFromCsv(context: Context): List<Movie> {
        val moviescsv = mutableListOf<Movie>()

        val inputStream = context.resources.openRawResource(R.raw.movies)
        inputStream.bufferedReader().use { reader ->
            // Skip the header
            reader.readLine()

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val tokens = line!!.split(",")
                if (tokens.size >= 6) {
                    // Assuming the title is in the 2nd column and the director in the 6th column
                    val title = tokens[1].trim('"')
                    val director = tokens[6].trim('"')
                    moviescsv.add(Movie(title, director))
                }
            }
        }

        return moviescsv
    }

    @Composable
    fun MovieListScreen(context: Context): List<Movie> {
        // Now you can use the movies list in your UI
        return loadMoviesFromCsv(context)
    }


    LaunchedEffect(triggerNextQuestion) {
        if (triggerNextQuestion) {
            showCorrectAnswer = true // Highlight the correct answer
            kotlinx.coroutines.delay(1000) // Wait for 1 second

            currentIndex++
            if (currentIndex >= loadMoviesFromCsv(context).size) currentIndex = 0

            showCorrectAnswer = false // Stop highlighting the correct answer
            triggerNextQuestion = false // Reset the trigger
        }
    }

    val movies = loadMoviesFromCsv(LocalContext.current)
    val currentMovie = movies.getOrNull((currentIndex*3 + 30) % loadMoviesFromCsv(context).size)
    val directors = loadDirectorsFromCsv(LocalContext.current)
    val directorOptions: List<String> by remember(currentIndex) {
        mutableStateOf(currentMovie?.let { generateDirectorOptions(it.director, directors) } ?: emptyList())
    }
    var selectedDirector by remember { mutableStateOf<String?>(null) }
    val onOptionSelected: (String) -> Unit = { director ->
        selectedDirector = director
        if (director == currentMovie?.director) {
            correctAnswers++
        } else {
            wrongAnswers++
        }
        triggerNextQuestion = true
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberImagePainter(data = "https://www2.bfi.org.uk/sites/bfi.org.uk/files/page/rashomon-1950-009-quad-poster-2022-1000x750.jpg", builder = {
                crossfade(true)
            }),
            contentDescription = "Movie Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Set your desired height
        )

        if (currentMovie != null) {
            Text(
                text = currentMovie.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
        MovieListScreen(context = context)
        if (currentMovie != null) {


            QuestionCard(
                movieTitle = currentMovie.title,
                directorOptions = directorOptions,
                correctDirector = currentMovie.director,
                selectedDirector = selectedDirector,
                showCorrectAnswer = showCorrectAnswer,
                onOptionSelected = onOptionSelected,

            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Space between QuestionCard and scores

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Correct: $correctAnswers",
                color = Color.Green,
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "Wrong: $wrongAnswers",
                color = Color.Red,
                style = MaterialTheme.typography.h6
            )
        }
    }
}





@Composable
fun QuestionCard(
    movieTitle: String,
    directorOptions: List<String>,
    correctDirector: String,
    selectedDirector: String?,
    showCorrectAnswer: Boolean,
    onOptionSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Select The Director",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            directorOptions.forEach { director ->
                val isCorrectAnswer = director == correctDirector
                val backgroundColor = when {
                    showCorrectAnswer && isCorrectAnswer -> Color.Green
                    else -> MaterialTheme.colors.surface
                }
                Button(
                    onClick = { onOptionSelected(director) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(director)
                }
            }
        }
    }
}


