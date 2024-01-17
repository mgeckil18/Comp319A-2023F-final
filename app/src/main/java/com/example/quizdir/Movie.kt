// data/models/Movie.kt
package com.example.quizdir

data class Movie(
    val title: String,
    var director: String
)


data class QuizQuestion(
    val movie: Movie,
    val options: List<String> // A list of director names, one of which is correct
)

