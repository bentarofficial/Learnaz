package com.learnaz.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.learnaz.app.BuildConfig
import com.learnaz.app.data.UpdateInfo
import com.learnaz.app.data.Updater
import kotlinx.coroutines.launch

/** Tự kiểm tra bản mới khi mở app; có bản mới thì hiện thẻ "Cập nhật". */
@Composable
fun UpdateBanner() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var info by remember { mutableStateOf<UpdateInfo?>(null) }
    var status by remember { mutableStateOf<String?>(null) }
    var progress by remember { mutableStateOf<Float?>(null) }

    LaunchedEffect(Unit) { info = Updater.check() }

    val update = info
    if (update == null) {
        Column {
            Text("Phiên bản ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = {
                status = "Đang kiểm tra..."
                scope.launch {
                    info = Updater.check()
                    status = if (info == null) "Bạn đang dùng bản mới nhất 😎" else null
                }
            }) { Text("🔄 Kiểm tra cập nhật") }
            status?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
        return
    }

    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text("🎁 Có bản mới ${update.versionName}!", fontWeight = FontWeight.Bold)
            if (update.notes.isNotBlank()) Text(update.notes, style = MaterialTheme.typography.bodySmall)
            progress?.let { p ->
                LinearProgressIndicator(progress = { p }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
            }
            status?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Button(
                enabled = progress == null,
                onClick = {
                    if (!Updater.canInstall(context)) {
                        status = "Hãy bật \"Cho phép cài từ nguồn này\" rồi quay lại bấm Cập nhật."
                        Updater.openInstallPermission(context)
                        return@Button
                    }
                    progress = 0f
                    status = "Mèo đang tải bản mới... 🐱💨"
                    scope.launch {
                        runCatching { Updater.download(context, update) { progress = it } }
                            .onSuccess { Updater.install(context, it); status = null }
                            .onFailure { status = "Tải lỗi rồi 😿 Kiểm tra mạng và thử lại." }
                        progress = null
                    }
                },
            ) { Text("⬇️ Cập nhật ngay") }
        }
    }
}
