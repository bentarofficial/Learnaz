# Cập nhật app từ GitHub

## Cách hoạt động
1. Mỗi lần đẩy code lên GitHub, workflow **Build & Release APK** sẽ build app và tạo một bản phát hành (Release) mới `v1.0.<số lần build>` kèm file `learnaz.apk`.
2. Khi mở app, Learnaz tự gọi GitHub để kiểm tra bản mới nhất. Nếu có bản mới, trang chủ hiện thẻ **🎁 Có bản mới → ⬇️ Cập nhật ngay**.
3. Bấm Cập nhật: app tải APK về rồi mở màn hình cài đặt của Android. Bấm **Cài đặt** là xong, dữ liệu học vẫn được giữ nguyên.
   Lần đầu, Android sẽ hỏi quyền *"Cho phép cài từ nguồn này"*: bạn bật quyền rồi quay lại bấm Cập nhật.

## Thiết lập một lần (bắt buộc)
Android chỉ cho cài đè khi mọi bản đều được **ký bằng cùng một khoá**. Hãy thêm 4 secret vào repo:
GitHub → repo → **Settings → Secrets and variables → Actions → New repository secret**

| Tên | Giá trị |
|---|---|
| `KEYSTORE_BASE64` | chuỗi base64 của file khoá |
| `KEYSTORE_PASSWORD` | mật khẩu khoá |
| `KEY_ALIAS` | `learnaz` |
| `KEY_PASSWORD` | mật khẩu khoá |

⚠️ Hãy cất giữ file khoá `.jks` cẩn thận. Nếu mất khoá, người dùng phải gỡ app rồi cài lại.

⚠️ Repo cần ở chế độ **Public** thì app mới đọc được Release mà không cần đăng nhập.
