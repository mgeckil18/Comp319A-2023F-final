
package com.example.quizdir

import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import java.io.BufferedReader
import java.io.InputStreamReader

class MovieViewModel(private val context: Context) : ViewModel(){




    // Method to get a random question with one correct and two random directors

}
class MovieViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



