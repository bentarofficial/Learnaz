package com.learnaz.app.data

/** Một từ vựng: kèm câu ví dụ "lầy" và mẹo nhớ bằng tiếng Việt. */
data class Word(
    val id: String,
    val en: String,
    val ipa: String,
    val vi: String,
    val example: String,
    val exampleVi: String,
    val trick: String,
)

data class Topic(val id: String, val emoji: String, val title: String, val words: List<Word>)

/** Một lượt hội thoại với Mèo Lầy. Người học chọn 1 trong các câu trả lời. */
data class ChatTurn(
    val botEn: String,
    val botVi: String,
    val options: List<ChatOption>,
)

data class ChatOption(
    val en: String,
    val correct: Boolean,
    val reaction: String, // Mèo Lầy phản ứng (tiếng Việt, tếu táo)
)

data class Story(val id: String, val emoji: String, val title: String, val turns: List<ChatTurn>)

object Content {
    val topics = listOf(
        Topic(
            "food", "🍜", "Ăn uống", listOf(
                Word("hungry", "hungry", "/ˈhʌŋɡri/", "đói",
                    "I'm so hungry I could eat my homework.",
                    "Tôi đói đến mức có thể ăn luôn bài tập về nhà.",
                    "\"Hăng-gri\": đói quá thì HĂNG máu, GRỪ GRỪ như hổ."),
                Word("delicious", "delicious", "/dɪˈlɪʃəs/", "ngon",
                    "My mom's pho is so delicious, the neighbors 'accidentally' visit at dinner.",
                    "Phở mẹ tôi ngon đến mức hàng xóm 'tình cờ' ghé đúng giờ cơm.",
                    "\"Đi-li-sợt\": ngon quá ĐI LIỀN, SỢ hết phần."),
                Word("spicy", "spicy", "/ˈspaɪsi/", "cay",
                    "This chili is so spicy my ears are crying.",
                    "Ớt này cay đến mức tai tôi cũng khóc.",
                    "\"Sờ-pai-xi\": ăn cay xong mặt đỏ như bị XÌ hơi."),
                Word("full", "full", "/fʊl/", "no",
                    "I'm full... but I still have room for bubble tea.",
                    "Tôi no rồi... nhưng vẫn còn chỗ cho trà sữa.",
                    "\"Phun\": ăn no quá suýt PHUN ra ngoài."),
                Word("leftovers", "leftovers", "/ˈleftˌoʊvərz/", "đồ ăn thừa",
                    "Leftovers taste better at 2 a.m.",
                    "Đồ ăn thừa ăn lúc 2 giờ sáng ngon hơn hẳn.",
                    "LEFT (bị bỏ lại) + OVER (còn dư) = phần bị bỏ lại."),
            )
        ),
        Topic(
            "feel", "😵", "Cảm xúc", listOf(
                Word("tired", "tired", "/ˈtaɪərd/", "mệt",
                    "I'm too tired to be tired.",
                    "Tôi mệt đến mức không còn sức để mệt nữa.",
                    "\"Tai-ợt\": mệt quá TAI ỢT cả lên."),
                Word("bored", "bored", "/bɔːrd/", "chán",
                    "I was so bored I read the shampoo bottle. Twice.",
                    "Tôi chán đến mức đọc chai dầu gội. Hai lần.",
                    "\"Bo\": chán quá ngồi BÓ gối."),
                Word("excited", "excited", "/ɪkˈsaɪtɪd/", "háo hức",
                    "The dog gets excited every time I open the fridge.",
                    "Con chó háo hức mỗi lần tôi mở tủ lạnh.",
                    "\"Ích-sai-tịt\": háo hức tới mức nói SAI TÍT mù."),
                Word("embarrassed", "embarrassed", "/ɪmˈbærəst/", "xấu hổ",
                    "I waved back at someone who was waving at the person behind me. Embarrassed.",
                    "Tôi vẫy lại người đang vẫy người sau lưng tôi. Quê độ.",
                    "\"Em-be-rợt\": xấu hổ như EM BÉ bị bắt quả tang."),
                Word("confused", "confused", "/kənˈfjuːzd/", "bối rối",
                    "My cat looks confused when I speak English to her.",
                    "Con mèo trông bối rối khi tôi nói tiếng Anh với nó.",
                    "\"Cần-phiu-dờ\": đầu óc như cầu chì nổ PHIU DỜ."),
            )
        ),
        Topic(
            "daily", "⏰", "Đời thường", listOf(
                Word("oversleep", "oversleep", "/ˌoʊvərˈsliːp/", "ngủ quên",
                    "I didn't oversleep, my alarm under-woke me.",
                    "Tôi không ngủ quên, chỉ là báo thức đánh thức chưa đủ mạnh.",
                    "OVER (quá) + SLEEP (ngủ) = ngủ quá giờ."),
                Word("procrastinate", "procrastinate", "/proʊˈkræstɪneɪt/", "trì hoãn",
                    "I'll learn the word 'procrastinate' tomorrow.",
                    "Mai tôi sẽ học từ 'trì hoãn'.",
                    "\"Pro-CRÁT-tì-nết\": dân PRO trong việc để mai tính."),
                Word("commute", "commute", "/kəˈmjuːt/", "đi làm (quãng đường)",
                    "My commute is 5 km and 2 hours of traffic jam.",
                    "Đường đi làm của tôi 5 km và 2 tiếng kẹt xe.",
                    "\"Cờ-miu\": kẹt xe ngồi CHỜ MIU MIU như mèo."),
                Word("broke", "broke", "/broʊk/", "cháy túi",
                    "It's only the 10th and I'm already broke.",
                    "Mới mùng 10 mà tôi đã cháy túi.",
                    "Ví BỂ (break → broke) nên tiền rơi hết."),
                Word("nap", "nap", "/næp/", "giấc ngủ ngắn",
                    "A 10-minute nap turned into a 3-hour adventure.",
                    "Giấc ngủ 10 phút biến thành chuyến phiêu lưu 3 tiếng.",
                    "\"Náp\": NẤP vào chăn chợp mắt tí."),
            )
        ),
    )

    val allWords: List<Word> get() = topics.flatMap { it.words }

    val stories = listOf(
        Story(
            "cafe", "☕", "Gọi đồ ở quán cà phê", listOf(
                ChatTurn("Hi! What can I get for you?", "Chào! Bạn muốn gọi gì?", listOf(
                    ChatOption("Can I have an iced coffee, please?", true, "Chuẩn không cần chỉnh! Lịch sự như quý tộc ☕"),
                    ChatOption("Give me coffee. Now.", false, "Ơ kìa, nghe như đi cướp quán chứ không phải gọi món 😾 Thêm 'please' vào nha!"),
                    ChatOption("I am a coffee.", false, "Bạn LÀ cà phê á? Vậy ai uống bạn? 🤯"),
                )),
                ChatTurn("Sure! What size would you like?", "Được! Bạn muốn cỡ nào?", listOf(
                    ChatOption("Large, please. I'm very tired.", true, "Cỡ lớn cho người mệt lớn. Hợp lý 💯"),
                    ChatOption("Size 42.", false, "Đó là cỡ giày đó bạn ơi 👟"),
                    ChatOption("Yes.", false, "Yes cái gì mà yes, tôi hỏi cỡ nào cơ mà 🙀"),
                )),
                ChatTurn("That'll be 50,000 dong.", "Của bạn 50 nghìn.", listOf(
                    ChatOption("Here you go. Keep the change!", true, "Hào phóng ghê! Mèo xin luôn tiền thối nha 💸"),
                    ChatOption("Can I pay with a hug?", false, "Dễ thương đấy, nhưng máy tính tiền không nhận ôm 🤗"),
                    ChatOption("Here you go.", true, "Ngắn gọn, chuẩn bài 👍"),
                )),
            )
        ),
        Story(
            "late", "🏃", "Đi làm muộn", listOf(
                ChatTurn("You're late again! Why?", "Bạn lại đi muộn! Tại sao?", listOf(
                    ChatOption("Sorry, I overslept.", true, "Thành thật là điểm cộng! Dù sếp vẫn cau mày 😼"),
                    ChatOption("Sorry, a dragon ate my bike.", false, "Sáng tạo 10 điểm, độ tin cậy 0 điểm 🐉"),
                    ChatOption("I am late again.", false, "Sếp biết rồi, sếp hỏi TẠI SAO cơ 😹"),
                )),
                ChatTurn("This is the third time this week!", "Đây là lần thứ ba trong tuần rồi!", listOf(
                    ChatOption("It won't happen again, I promise.", true, "Lời hứa kinh điển. Mèo ghi âm lại làm bằng chứng 🎙️"),
                    ChatOption("Only three? I'm improving!", false, "Lầy ghê! Nhưng đừng nói thế với sếp thật nha 😂"),
                    ChatOption("Thank you!", false, "Sếp đang mắng chứ có khen đâu mà cảm ơn 🙈"),
                )),
            )
        ),
        Story(
            "date", "💘", "Hẹn hò lần đầu", listOf(
                ChatTurn("So... what do you do for fun?", "Vậy... bạn thích làm gì cho vui?", listOf(
                    ChatOption("I like watching movies and eating snacks.", true, "Sở thích quốc dân, an toàn tuyệt đối 🍿"),
                    ChatOption("I count my cat's hair.", false, "Hơi... đáng sợ. Mèo đề nghị đổi chủ đề 🙀"),
                    ChatOption("Fun is a noun.", false, "Đúng ngữ pháp nhưng mất điểm duyên dáng 📉"),
                )),
                ChatTurn("Cool! Do you want to see a movie this weekend?", "Hay đó! Cuối tuần đi xem phim không?", listOf(
                    ChatOption("I'd love to!", true, "Đẹp! 'I'd love to' = rất sẵn lòng. Mèo ship cặp này 💕"),
                    ChatOption("Only if you pay.", false, "Thật thà quá mức cần thiết 💸"),
                    ChatOption("I'm busy forever.", false, "Từ chối kiểu này người ta tổn thương đó 😿"),
                )),
            )
        ),
    )

    val correctCheers = listOf(
        "Đỉnh nóc kịch trần! 🚀", "Não bạn đang phát sáng ✨", "Chuẩn như sách giáo khoa 📚",
        "Mèo vỗ tay bằng 4 chân 🐾", "Thầy giáo tiếng Anh rơi nước mắt tự hào 🥲",
    )
    val wrongJokes = listOf(
        "Sai rồi, nhưng sai một cách tự tin 😎", "Không sao, Mèo cũng từng sai... hôm qua 🙃",
        "Suýt đúng! Chỉ lệch khoảng... một từ 🤏", "Sai là mẹ thành công, bạn sắp làm mẹ rồi 👶",
        "Câu này khó, không phải tại bạn đâu (một phần thôi) 😹",
    )
}
