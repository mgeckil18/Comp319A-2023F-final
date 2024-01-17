package com.example.quizdir

import android.annotation.SuppressLint
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.asd),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Adjust scaling to fit your needs
                )

                // Your app's navigation and UI
                val isDarkThemeEnabled = rememberSaveable { mutableStateOf(true) }

                QuizDirTheme(darkTheme = isDarkThemeEnabled.value) {
                    // Pass the state and state change function down to the QuizDirApp
                    QuizDirApp(isDarkThemeEnabled)
                }
            }
            BiometricButton(onClick = {
                // Trigger biometric prompt
            })
        }
        /*
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt()
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)

         */
    }

}

private fun BiometricPrompt.authenticate(promptInfo: Any) {

}

@Composable
fun BiometricButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Authenticate")
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QuizDirApp(isDarkThemeEnabled: MutableState<Boolean>) {


    val navController = rememberNavController()
    Scaffold(
        topBar = {
            AppBar(
                title = "QuizDir",
                onNavigateUp = { /* Define up navigation if needed */ },
                onToggleTheme = { isDarkThemeEnabled.value = !isDarkThemeEnabled.value }
            )
        }
        // Existing Scaffold and TopAppBar code
    ) {
        NavHost(navController = navController, startDestination = "main") {
            composable("main") { MainScreen(navController) }
            composable("scoreboard") { ScoreBoard(navController) }
            composable("quiz") { QuizScreen(navController) }
            // Add other routes and composable screens here...
        }
    }
}

@Composable
fun AppBar(title: String, onNavigateUp: (() -> Unit)? = null, onToggleTheme: () -> Unit, navController: NavController? = null) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            // Theme switch icon
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (MaterialTheme.colors.isLight) Icons.Default.Edit else Icons.Default.Edit,
                    contentDescription = "Toggle theme"
                )
            }
        },
        navigationIcon = navController?.let { nav ->

                {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }

        }
    )
}

private val LightColorPalette = lightColors(
    primary = Color(0xFf6288EE),
    primaryVariant = Color(0xFF3755B3),
    secondary = Color(0xFF03DAC5),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF121212),
    onBackground = Color(0xFF121212),
    onSurface = Color(0xFF121212),
)

private val DarkColorPalette = darkColors(
    primary = Color(0xFF6686FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
)

// Replace with your own typography
private val AppTypography = Typography()
private val AppShapes = Shapes()

@Composable
fun QuizDirTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}

