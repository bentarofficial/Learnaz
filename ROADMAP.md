# Learnaz – Roadmap

App học tiếng Anh cho người Việt trên Android (Kotlin + Jetpack Compose).
Tinh thần: **vừa học vừa tán gẫu tếu táo với Mèo Lầy 🐱 – vui mà nhớ lâu.**

## ✅ Giai đoạn 0 – Bộ khung (đã có)
- Trang chủ: chuỗi ngày học 🔥, XP ⭐, số từ đã thuộc 🧠
- Thẻ từ vựng có lật thẻ, câu ví dụ hài, **mẹo nhớ bằng tiếng Việt**
- Ôn tập kiểu hộp Leitner: từ hay quên được hỏi lại trước
- Trắc nghiệm 10 câu, đúng thì được khen, sai thì bị Mèo cà khịa kèm mẹo nhớ
- Hội thoại theo tình huống (quán cà phê, đi làm muộn, hẹn hò) với câu trả lời ngớ ngẩn để cười
- Phát âm bằng giọng đọc có sẵn trên máy (Android TextToSpeech)
- GitHub Actions tự build file APK

## 🔜 Giai đoạn 1 – Giọng đọc VieNeu TTS
- **VieNeu-TTS**: mô hình đọc tiếng Việt mã nguồn mở, miễn phí, có thể nhân bản giọng.
  Dùng để Mèo Lầy **nói tiếng Việt** (lời khen, lời cà khịa, giải thích mẹo nhớ) bằng giọng tự nhiên, cá tính.
- Tiếng Anh vẫn dùng TTS của Android (hoặc một mô hình tiếng Anh miễn phí).
- Hai cách tích hợp (cần thử nghiệm để chọn):
  1. **Tạo sẵn file âm thanh** cho nội dung cố định → đóng gói vào app, chạy offline, không tốn máy chủ. *(Nên làm trước)*
  2. **Máy chủ riêng** chạy VieNeu-TTS cho câu do AI sinh ra → cần GPU/CPU server, có độ trễ.
- Việc cần làm: kiểm tra giấy phép sử dụng, dung lượng model, chất lượng trên điện thoại tầm trung, thử bản nhỏ/lượng tử hoá chạy thẳng trên máy.

## 🔜 Giai đoạn 2 – Kết nối Claude để học với AI
Mục tiêu: mở app → kết nối Claude → Mèo Lầy trò chuyện tự do, sửa lỗi tiếng Anh một cách hài hước.

**Lưu ý quan trọng:** tài khoản Claude.ai (gói Free/Pro) hiện **không có cách đăng nhập chính thức để app bên thứ ba dùng**.
Các lựa chọn khả thi:
| Cách | Ưu | Nhược |
|---|---|---|
| A. Người dùng dán **API key** của Anthropic (console.anthropic.com) | Làm nhanh, không cần máy chủ | Người dùng phải có key và tự trả phí; lưu key an toàn bằng EncryptedSharedPreferences/Keystore |
| B. **Máy chủ riêng** giữ API key, app đăng nhập bằng tài khoản Learnaz | Trải nghiệm dễ nhất, kiểm soát chi phí | Phải vận hành backend và chịu chi phí API |

Đề xuất: làm **A** để thử nghiệm, sau đó chuyển sang **B** khi có người dùng thật.

Tính năng AI:
- Chat tự do theo vai (khách hàng khó tính, sếp, crush...) với system prompt "Mèo Lầy"
- Sửa câu sai + giải thích ngắn bằng tiếng Việt, kèm một câu đùa
- Sinh thêm câu ví dụ hài và mẹo nhớ cho từ mới
- Câu trả lời của AI được đọc bằng VieNeu TTS (giai đoạn 1, cách 2)

## 🔮 Giai đoạn 3 – Mở rộng
- Luyện phát âm: nhận dạng giọng nói (SpeechRecognizer) và chấm điểm
- Thêm nhiều chủ đề và cấp độ (A1 → B2)
- Nhắc học hằng ngày bằng thông báo hài ("Mèo nhớ bạn 😿")
- Huy hiệu, bảng xếp hạng bạn bè
- Chế độ tối, icon và mascot riêng
