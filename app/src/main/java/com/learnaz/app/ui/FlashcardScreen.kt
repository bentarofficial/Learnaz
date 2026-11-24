package com.learnaz.app.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learnaz.app.data.Progress
import com.learnaz.app.data.Speaker
import com.learnaz.app.data.Topic

@Composable
fun FlashcardScreen(topic: Topic, progress: Progress, speaker: Speaker, onBack: () -> Unit) {
    val queue = remember(topic) { progress.reviewQueue(topic.words) }
    var index by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    var remembered by remember { mutableIntStateOf(0) }
    val rotation by animateFloatAsState(if (flipped) 180f else 0f, label = "flip")

    Page("${topic.emoji} ${topic.title}", onBack) {
        if (index >= queue.size) {
            Done("Xong bộ thẻ! 🎉", "Bạn nhớ $remembered/${queue.size} từ.\nMèo chấm: ${if (remembered == queue.size) "thiên tài 🧠" else "ổn áp, ôn tiếp nha 😺"}", onBack)
            return@Page
        }
        val w = queue[index]
        LinearProgressIndicator(
            progress = { index / queue.size.toFloat() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        )
        Text("Chạm vào thẻ để lật", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))

        Card(
            Modifier.fillMaxWidth().weight(1f)
                .graphicsLayer { rotationY = rotation; cameraDistance = 12 * density }
                .clickable { flipped = !flipped },
        ) {
            Box(Modifier.fillMaxWidth().weight(1f).padding(20.dp), contentAlignment = Alignment.Center) {
                if (rotation <= 90f) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(w.en, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        Text(w.ipa, fontSize = 18.sp)
                        TextButton(onClick = { speaker.say(w.en) }) { Text("🔊 Nghe") }
                    }
                } else {
                    Column(
                        Modifier.graphicsLayer { rotationY = 180f }.verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(w.vi, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Text("“${w.example}”", textAlign = TextAlign.Center, fontStyle = FontStyle.Italic)
                        Text(w.exampleVi, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                        Text("💡 ${w.trick}", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.secondary)
                        TextButton(onClick = { speaker.say(w.example) }) { Text("🔊 Nghe câu") }
                    }
                }
            }
        }

        Row(Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { progress.answer(w.id, false); flipped = false; index++ },
                modifier = Modifier.weight(1f),
            ) { Text("😵 Quên rồi") }
            Button(
                onClick = { progress.answer(w.id, true); remembered++; flipped = false; index++ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Good),
            ) { Text("😎 Nhớ!") }
        }
    }
}

@Composable
fun Done(title: String, body: String, onBack: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("🐱", fontSize = 72.sp)
        Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(body, textAlign = TextAlign.Center)
        Button(onClick = onBack) { Text("Về trang chủ") }
    }
}
