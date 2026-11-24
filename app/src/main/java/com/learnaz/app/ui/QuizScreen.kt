package com.learnaz.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learnaz.app.data.Content
import com.learnaz.app.data.Progress
import com.learnaz.app.data.Speaker
import com.learnaz.app.data.Word

private data class Question(val word: Word, val options: List<String>)

@Composable
fun QuizScreen(progress: Progress, speaker: Speaker, onBack: () -> Unit) {
    val questions = remember {
        val all = Content.allWords
        progress.reviewQueue(all).take(10).map { w ->
            Question(w, (all.filter { it.id != w.id }.shuffled().take(3).map { it.vi } + w.vi).shuffled())
        }
    }
    var index by remember { mutableIntStateOf(0) }
    var picked by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Page("⚡ Trắc nghiệm", onBack) {
        if (index >= questions.size) {
            Done("$score/${questions.size} câu đúng", if (score >= 8) "Mèo xin bái sư 🙇" else "Không sao, sai nhiều mới nhớ lâu 😹", onBack)
            return@Page
        }
        val q = questions[index]
        LinearProgressIndicator(progress = { index / questions.size.toFloat() }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        Spacer(Modifier.height(24.dp))
        Text("\"${q.word.en}\" nghĩa là gì?", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        OutlinedButton(onClick = { speaker.say(q.word.en) }) { Text("🔊 ${q.word.ipa}") }
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            q.options.forEach { opt ->
                val color = when {
                    picked == null -> ButtonDefaults.buttonColors()
                    opt == q.word.vi -> ButtonDefaults.buttonColors(containerColor = Good)
                    opt == picked -> ButtonDefaults.buttonColors(containerColor = Bad)
                    else -> ButtonDefaults.buttonColors()
                }
                Button(
                    onClick = {
                        if (picked == null) {
                            picked = opt
                            val ok = opt == q.word.vi
                            if (ok) score++
                            progress.answer(q.word.id, ok)
                            comment = if (ok) Content.correctCheers.random() else Content.wrongJokes.random() + "\n💡 " + q.word.trick
                        }
                    },
                    colors = color, modifier = Modifier.fillMaxWidth(),
                ) { Text(opt) }
            }
        }
        if (picked != null) {
            Spacer(Modifier.height(16.dp))
            Text(comment)
            Spacer(Modifier.height(12.dp))
            Button(onClick = { picked = null; index++ }, modifier = Modifier.fillMaxWidth()) { Text("Câu tiếp →") }
        }
    }
}
