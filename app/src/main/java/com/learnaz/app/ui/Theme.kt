package com.learnaz.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.learnaz.app.data.Story
import com.learnaz.app.data.Topic

sealed interface Screen {
    data object Home : Screen
    data class Flashcards(val topic: Topic) : Screen
    data object Quiz : Screen
    data class Chat(val story: Story) : Screen
    data object AiChat : Screen
    data object AiSettings : Screen
}

val Good = Color(0xFF2E9E5B)
val Bad = Color(0xFFD9534F)

@Composable
fun LearnazTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) {
        darkColorScheme(primary = Color(0xFFFFB74D), secondary = Color(0xFF80CBC4))
    } else {
        lightColorScheme(primary = Color(0xFFF57C00), secondary = Color(0xFF00897B))
    }
    MaterialTheme(colorScheme = colors, content = content)
}
