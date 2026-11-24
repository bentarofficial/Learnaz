package com.learnaz.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learnaz.app.data.Content
import com.learnaz.app.data.Progress

private val greetings = listOf(
    "Hôm nay học tí cho não đỡ buồn nha 🐱",
    "Mèo đợi bạn nãy giờ, tưởng bạn bỏ Mèo rồi 😿",
    "5 phút mỗi ngày, giỏi tiếng Anh lúc nào không hay 😼",
    "Học đi, không thì Mèo nói tiếng Anh giỏi hơn bạn đó 😹",
)

@Composable
fun HomeScreen(progress: Progress, refresh: Int, go: (Screen) -> Unit) {
    val greeting = remember(refresh) { greetings.random() }
    val xp = remember(refresh) { progress.xp }
    val streak = remember(refresh) { progress.streak }
    val learned = remember(refresh) { progress.learnedCount() }

    Page("Learnaz 🐱", onBack = null) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text(greeting, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Stat("🔥", "$streak ngày", Modifier.weight(1f))
                    Stat("⭐", "$xp XP", Modifier.weight(1f))
                    Stat("🧠", "$learned/${Content.allWords.size} từ", Modifier.weight(1f))
                }
            }
            item { UpdateBanner() }
            item { Section("🤖 Mèo AI") }
            item { MenuCard("😹", "Chém gió tự do với Mèo AI", "Gõ gì cũng được, Mèo sửa lỗi kiểu lầy") { go(Screen.AiChat) } }
            item { MenuCard("⚙️", "Kết nối AI (Kyma API)", "Nhập key và chọn model") { go(Screen.AiSettings) } }
            item { Section("💬 Tán gẫu với Mèo Lầy") }
            items(Content.stories) { s ->
                MenuCard(s.emoji, s.title, "${s.turns.size} lượt thoại") { go(Screen.Chat(s)) }
            }
            item { Section("🃏 Thẻ từ vựng") }
            items(Content.topics) { t ->
                MenuCard(t.emoji, t.title, "${t.words.size} từ · có mẹo nhớ hài") { go(Screen.Flashcards(t)) }
            }
            item { Section("🎯 Thử thách") }
            item { MenuCard("⚡", "Trắc nghiệm nhanh", "10 câu, trả lời sai bị Mèo cà khịa") { go(Screen.Quiz) } }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun Section(text: String) =
    Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))

@Composable
private fun Stat(emoji: String, label: String, modifier: Modifier) {
    Card(modifier) {
        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 24.sp)
            Text(label, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun MenuCard(emoji: String, title: String, sub: String, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 32.sp)
            Column(Modifier.padding(start = 16.dp)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(sub, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
