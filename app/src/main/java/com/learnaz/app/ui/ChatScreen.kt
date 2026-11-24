package com.learnaz.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.learnaz.app.data.Progress
import com.learnaz.app.data.Speaker
import com.learnaz.app.data.Story

private data class Bubble(val fromMe: Boolean, val text: String, val sub: String? = null, val speak: String? = null)

@Composable
fun ChatScreen(story: Story, progress: Progress, speaker: Speaker, onBack: () -> Unit) {
    val bubbles = remember { mutableStateListOf<Bubble>() }
    var turn by remember { mutableIntStateOf(0) }
    var correct by remember { mutableIntStateOf(0) }
    val list = rememberLazyListState()

    LaunchedEffect(turn) {
        story.turns.getOrNull(turn)?.let {
            bubbles += Bubble(false, it.botEn, it.botVi, it.botEn)
            speaker.say(it.botEn)
        }
    }
    LaunchedEffect(bubbles.size) { if (bubbles.isNotEmpty()) list.animateScrollToItem(bubbles.size - 1) }

    Page("${story.emoji} ${story.title}", onBack) {
        LazyColumn(Modifier.weight(1f), state = list, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bubbles) { b -> ChatBubble(b) { b.speak?.let(speaker::say) } }
        }
        val current = story.turns.getOrNull(turn)
        Column(Modifier.padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (current != null) {
                remember(turn) { current.options.shuffled() }.forEach { opt ->
                    OutlinedButton(onClick = {
                        bubbles += Bubble(true, opt.en, speak = opt.en)
                        bubbles += Bubble(false, "🐱 " + opt.reaction)
                        if (opt.correct) { correct++; progress.addXp(15) }
                        speaker.say(opt.en)
                        if (opt.correct) turn++
                    }, modifier = Modifier.fillMaxWidth()) { Text(opt.en) }
                }
            } else {
                Text("Hết truyện! Bạn trả lời chuẩn $correct lần 🎉")
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Về trang chủ") }
            }
        }
    }
}

@Composable
private fun ChatBubble(b: Bubble, onTap: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (b.fromMe) Arrangement.End else Arrangement.Start) {
        Column(
            Modifier.widthIn(max = 300.dp)
                .background(
                    if (b.fromMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(16.dp),
                )
                .clickable(onClick = onTap)
                .padding(12.dp),
        ) {
            Text(b.text)
            b.sub?.let { Text(it, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic) }
        }
    }
}
