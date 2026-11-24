package com.learnaz.app.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AiMessage(val role: String, val content: String)

/** Cài đặt AI: key Kyma và model, lưu trong bộ nhớ riêng của app. */
class AiSettings(context: Context) {
    private val prefs = context.getSharedPreferences("learnaz_ai", Context.MODE_PRIVATE)
    var apiKey: String
        get() = prefs.getString("key", "") ?: ""
        set(v) = prefs.edit().putString("key", v.trim()).apply()
    var model: String
        get() = prefs.getString("model", DEFAULT_MODEL) ?: DEFAULT_MODEL
        set(v) = prefs.edit().putString("model", v.trim()).apply()

    companion object { const val DEFAULT_MODEL = "claude-sonnet-4-5" }
}

/** Gọi Kyma API (chuẩn OpenAI: /v1/chat/completions). */
object KymaClient {
    private const val BASE = "https://kymaapi.com/v1"

    const val SYSTEM_PROMPT = """Bạn là "Mèo Lầy", một con mèo hài hước dạy tiếng Anh cho người Việt.
Quy tắc:
- Trò chuyện bằng tiếng Anh ĐƠN GIẢN (trình độ A2-B1), câu ngắn.
- Nếu người học viết sai, sửa ngay ở đầu câu trả lời theo dạng: "✏️ Sửa: <câu đúng>" kèm giải thích cực ngắn bằng tiếng Việt và một câu đùa nhẹ.
- Sau đó trả lời tiếp câu chuyện và LUÔN kết thúc bằng một câu hỏi để người học nói tiếp.
- Thỉnh thoảng dạy 1 từ mới hay, kèm nghĩa tiếng Việt và mẹo nhớ hài hước.
- Giọng điệu: lầy lội, tếu táo, thân thiện, không xúc phạm. Dùng emoji mèo 🐱😹.
- Tối đa khoảng 80 từ mỗi lượt."""

    private fun open(path: String, key: String) =
        (URL("$BASE$path").openConnection() as HttpURLConnection).apply {
            setRequestProperty("Authorization", "Bearer $key")
            setRequestProperty("Content-Type", "application/json")
            connectTimeout = 15_000
            readTimeout = 60_000
        }

    private fun HttpURLConnection.readOrThrow(): String {
        val ok = responseCode in 200..299
        val text = (if (ok) inputStream else errorStream)?.bufferedReader()?.use { it.readText() }.orEmpty()
        if (!ok) throw RuntimeException(
            when (responseCode) {
                401, 403 -> "Key Kyma không hợp lệ 😿 Kiểm tra lại trong Cài đặt."
                402 -> "Tài khoản Kyma hết tiền rồi 💸"
                429 -> "Gọi nhanh quá, Mèo hụt hơi. Đợi chút nha 😮‍💨"
                else -> "Lỗi $responseCode: ${text.take(200)}"
            }
        )
        return text
    }

    suspend fun chat(settings: AiSettings, history: List<AiMessage>): String = withContext(Dispatchers.IO) {
        val messages = JSONArray().put(JSONObject().put("role", "system").put("content", SYSTEM_PROMPT))
        history.forEach { messages.put(JSONObject().put("role", it.role).put("content", it.content)) }
        val body = JSONObject().put("model", settings.model).put("messages", messages).put("max_tokens", 400)
        val conn = open("/chat/completions", settings.apiKey).apply {
            requestMethod = "POST"
            doOutput = true
            outputStream.use { it.write(body.toString().toByteArray()) }
        }
        JSONObject(conn.readOrThrow()).getJSONArray("choices").getJSONObject(0)
            .getJSONObject("message").getString("content").trim()
    }

    suspend fun models(key: String): List<String> = withContext(Dispatchers.IO) {
        val data = JSONObject(open("/models", key).readOrThrow()).getJSONArray("data")
        (0 until data.length()).map { data.getJSONObject(it).getString("id") }.sorted()
    }
}
