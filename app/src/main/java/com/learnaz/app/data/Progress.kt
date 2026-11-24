package com.learnaz.app.data

import android.content.Context
import android.speech.tts.TextToSpeech
import java.time.LocalDate
import java.util.Locale

/**
 * Lưu XP, chuỗi ngày học và hộp ôn tập (Leitner) cho từng từ.
 * Hộp 0..5: trả lời đúng lên hộp, sai về hộp 0. Từ ở hộp thấp được ôn trước.
 */
class Progress(context: Context) {
    private val prefs = context.getSharedPreferences("learnaz", Context.MODE_PRIVATE)

    val xp: Int get() = prefs.getInt("xp", 0)
    val streak: Int get() = prefs.getInt("streak", 0)

    fun box(wordId: String): Int = prefs.getInt("box_$wordId", 0)

    fun learnedCount(): Int = Content.allWords.count { box(it.id) >= 3 }

    fun answer(wordId: String, correct: Boolean) {
        val newBox = if (correct) (box(wordId) + 1).coerceAtMost(5) else 0
        prefs.edit().putInt("box_$wordId", newBox).apply()
        if (correct) addXp(10)
    }

    fun addXp(amount: Int) {
        touchStreak()
        prefs.edit().putInt("xp", xp + amount).apply()
    }

    /** Từ cần ôn nhất trước, xáo trộn trong cùng hộp cho đỡ nhàm. */
    fun reviewQueue(words: List<Word>): List<Word> =
        words.shuffled().sortedBy { box(it.id) }

    private fun touchStreak() {
        val today = LocalDate.now().toEpochDay()
        val last = prefs.getLong("lastDay", -1)
        val newStreak = when (last) {
            today -> streak
            today - 1 -> streak + 1
            else -> 1
        }
        prefs.edit().putLong("lastDay", today).putInt("streak", newStreak).apply()
    }
}

/** Đọc to tiếng Anh bằng giọng có sẵn của máy. */
class Speaker(context: Context) {
    private var ready = false
    private val tts = TextToSpeech(context.applicationContext) { status ->
        ready = status == TextToSpeech.SUCCESS
    }

    fun say(text: String) {
        if (ready) {
            tts.language = Locale.US
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
        }
    }

    fun shutdown() = tts.shutdown()
}
