package com.learnaz.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.learnaz.app.data.AiMessage
import com.learnaz.app.data.AiSettings
import com.learnaz.app.data.KymaClient
import com.learnaz.app.data.Progress
import com.learnaz.app.data.Speaker
import kotlinx.coroutines.launch

@Composable
fun AiSettingsScreen(settings: AiSettings, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var key by remember { mutableStateOf(settings.apiKey) }
    var model by remember { mutableStateOf(settings.model) }
    var models by remember { mutableStateOf<List<String>>(emptyList()) }
    var status by remember { mutableStateOf<String?>(null) }

    Page("⚙️ Kết nối AI (Kyma)", onBack) {
        Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Lấy key tại kymaapi.com (key bắt đầu bằng \"kyma-\"). Key chỉ lưu trên máy bạn.")
            OutlinedTextField(key, { key = it }, label = { Text("Kyma API key") },
                singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(model, { model = it }, label = { Text("Model") },
                singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedButton(onClick = {
                status = "Đang tải danh sách model..."
                scope.launch {
                    runCatching { KymaClient.models(key.trim()) }
                        .onSuccess { models = it; status = "Có ${it.size} model, chạm để chọn 👇" }
                        .onFailure { status = it.message }
                }
            }) { Text("📋 Tải danh sách model") }
            status?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Button(onClick = { settings.apiKey = key; settings.model = model; onBack() }, modifier = Modifier.fillMaxWidth()) {
                Text("💾 Lưu")
            }
            models.forEach { m ->
                Text(
                    (if (m == model) "✅ " else "• ") + m,
                    Modifier.fillMaxWidth().clickable { model = m }.padding(vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
fun AiChatScreen(settings: AiSettings, progress: Progress, speaker: Speaker, onBack: () -> Unit, onSettings: () -> Unit) {
    val scope = rememberCoroutineScope()
    val history = remember { mutableStateListOf<AiMessage>() }
    var input by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val list = rememberLazyListState()

    fun send(text: String) {
        history += AiMessage("user", text)
        loading = true; error = null
        scope.launch {
            runCatching { KymaClient.chat(settings, history.toList()) }
                .onSuccess { history += AiMessage("assistant", it); progress.addXp(5) }
                .onFailure { error = it.message ?: "Lỗi mạng 😿" }
            loading = false
        }
    }

    LaunchedEffect(Unit) {
        if (settings.apiKey.isNotBlank() && history.isEmpty()) send("Hi! Let's chat. Start with a fun question for me.")
    }
    LaunchedEffect(history.size) { if (history.isNotEmpty()) list.animateScrollToItem(history.size - 1) }

    Page("🤖 Chém gió với Mèo AI", onBack) {
        if (settings.apiKey.isBlank()) {
            Text("Chưa có key Kyma. Vào Cài đặt để kết nối nha 🐱", Modifier.padding(vertical = 24.dp))
            Button(onClick = onSettings) { Text("⚙️ Mở Cài đặt") }
            return@Page
        }
        LazyColumn(Modifier.weight(1f), state = list, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(history.drop(1)) { m ->
                val me = m.role == "user"
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if (me) Arrangement.End else Arrangement.Start) {
                    Text(
                        m.content,
                        Modifier.widthIn(max = 300.dp)
                            .background(
                                if (me) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(16.dp),
                            )
                            .clickable { speaker.say(m.content) }
                            .padding(12.dp),
                    )
                }
            }
            if (loading) item { CircularProgressIndicator(Modifier.padding(8.dp)) }
        }
        error?.let { Text(it, color = Bad) }
        Row(Modifier.fillMaxWidth().imePadding().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(input, { input = it }, Modifier.weight(1f).heightIn(max = 120.dp),
                placeholder = { Text("Gõ tiếng Anh, sai cũng được 😹") })
            Button(
                enabled = input.isNotBlank() && !loading,
                onClick = { send(input.trim()); input = "" },
                modifier = Modifier.padding(start = 8.dp),
            ) { Text("Gửi") }
        }
        Text("Chạm vào tin nhắn để nghe đọc 🔊", style = MaterialTheme.typography.bodySmall)
    }
}
