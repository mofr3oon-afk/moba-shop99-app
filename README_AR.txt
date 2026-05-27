MOBA SHOP99 - فرعون مساعد الشحن

دي نسخة اندرويد Native بسيطة وليست موقع WebView.
التطبيق فيه حاجتين بس:
1) فرعون المساعد لاستقبال الطلب خطوة بخطوة.
2) سجل الطلبات للعميل.

الموجود في النسخة:
- اختيار اللعبة.
- اختيار الباقة.
- إدخال ID واسم الحساب ورقم الموبايل.
- اختيار طريقة الدفع.
- زر رفع صورة التحويل عن طريق اختيار صورة من الهاتف.
- زر فتح InstaPay.
- تنفيذ الطلب وحفظه في سجل الطلبات.
- إمكانية إرسال الطلب تلقائيا لتليجرام لو حطيت بيانات البوت.

تشغيل المشروع:
1) افتح Android Studio.
2) اختار Open وافتح فولدر MOBA_SHOP99_Assistant.
3) استنى Gradle Sync.
4) دوس Run على موبايل اندرويد أو Emulator.
5) عشان تطلع APK: Build > Generate App Bundles or APKs > Generate APK.

تفعيل إرسال الطلبات لتليجرام:
افتح الملف:
app/src/main/java/com/mobashop99/assistant/Config.java

وحط:
TELEGRAM_BOT_TOKEN
ADMIN_CHAT_ID

لو سيبتهم فاضيين، الطلب هيتحفظ في سجل الطلبات بس، وهيظهر زر إرسال للدعم.

مهم قبل Google Play:
- لازم تعمل Privacy Policy.
- ممنوع تكتب إن التطبيق رسمي تابع لأي لعبة.
- استخدم وصف: خدمة شحن رقمية مستقلة من MOBA SHOP99.

طريقة استخراج APK من الموبايل بدون Android Studio:
1) ارفع فولدر المشروع على GitHub كـ Repository.
2) افتح تبويب Actions.
3) اختار Build Android APK.
4) دوس Run workflow.
5) بعد ما يخلص افتح آخر Run وانزل ملف Artifacts باسم MOBA-SHOP99-debug-apk.
6) هتلاقي جواه app-debug.apk تثبته على الموبايل.
