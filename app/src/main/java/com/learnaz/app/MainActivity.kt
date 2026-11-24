package com.learnaz.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.learnaz.app.data.AiSettings
import com.learnaz.app.data.Progress
import com.learnaz.app.ui.AiChatScreen
import com.learnaz.app.ui.AiSettingsScreen
import com.learnaz.app.data.Speaker
import com.learnaz.app.ui.ChatScreen
import com.learnaz.app.ui.FlashcardScreen
import com.learnaz.app.ui.HomeScreen
import com.learnaz.app.ui.LearnazTheme
import com.learnaz.app.ui.QuizScreen
import com.learnaz.app.ui.Screen

class MainActivity : ComponentActivity() {
    private lateinit var speaker: Speaker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        speaker = Speaker(this)
        val progress = Progress(this)
        val aiSettings = AiSettings(this)

        setContent {
            LearnazTheme {
                var screen by remember { mutableStateOf<Screen>(Screen.Home) }
                // Tăng mỗi khi quay về Home để màn hình đọc lại XP/streak mới.
                var refresh by remember { mutableIntStateOf(0) }
                val goHome = { screen = Screen.Home; refresh++ }

                BackHandler(enabled = screen != Screen.Home) { goHome() }

                when (val s = screen) {
                    Screen.Home -> HomeScreen(progress, refresh) { screen = it }
                    is Screen.Flashcards -> FlashcardScreen(s.topic, progress, speaker, goHome)
                    Screen.Quiz -> QuizScreen(progress, speaker, goHome)
                    is Screen.Chat -> ChatScreen(s.story, progress, speaker, goHome)
                    Screen.AiChat -> AiChatScreen(aiSettings, progress, speaker, goHome) { screen = Screen.AiSettings }
                    Screen.AiSettings -> AiSettingsScreen(aiSettings, goHome)
                }
            }
        }
    }

    override fun onDestroy() {
        speaker.shutdown()
        super.onDestroy()
    }
}
