package com.example.quizdir
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class QuizViewModel(private val context: Context)  : ViewModel() {
    private val movies: List<Movie> = loadMoviesFromCsv()
    private val directors: List<String> = loadDirectorsFromCsv()

    var currentIndex by mutableStateOf(0)
    var correctAnswers by mutableStateOf(0)
    var wrongAnswers by mutableStateOf(0)
    var selectedDirector: String? by mutableStateOf(null)

    val currentMovie: Movie?
        get() = movies.getOrNull((currentIndex * 3 + 30) % movies.size)

    val directorOptions: List<String>
        get() = currentMovie?.let { generateDirectorOptions(it.director) } ?: emptyList()

    fun onOptionSelected(director: String) {
        selectedDirector = director
        if (director == currentMovie?.director) {
            correctAnswers++
        } else {
            wrongAnswers++
        }
        moveToNextQuestion()
    }

    fun moveToNextQuestion() {
        currentIndex = (currentIndex + 1) % movies.size
        selectedDirector = null // Reset the selected director
    }

    private fun loadDirectorsFromCsv(): List<String> {
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


    private fun generateDirectorOptions(correctDirector: String): List<String> {
        val options = mutableSetOf(correctDirector)
        while (options.size < 4) {
            options.add(directors.random())
        }
        return options.shuffled()
    }


    private fun loadMoviesFromCsv(): List<Movie> {
        val movies = mutableListOf<Movie>()

        val inputStream = context.resources.openRawResource(R.raw.movies) // Adjust the file name accordingly
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
                    movies.add(Movie(title, director))
                }
            }
        }

        return movies
    }


    // Additional functions and logic as needed
}
