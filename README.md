# 🎭 Guess The Celebrity — Android Game (Jetpack Compose + Material 3)

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.0-purple?logo=kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=android" />
  <img src="https://img.shields.io/badge/Jsoup-HTML%20Parser-orange?logo=java" />
  <img src="https://img.shields.io/badge/Coil-Image%20Loader-FF6F00?logo=kotlin" />
  <img src="https://img.shields.io/badge/Coroutines-Asynchronous-blueviolet?logo=kotlin" />
</p>

---

## 🌟 Описание проекта

**Guess The Celebrity** — это Android-приложение, которое позволяет вам проверить, насколько хорошо вы знаете мировых знаменитостей.  
Приложение загружает список **100 самых популярных знаменитостей в мире** (например, с IMDb) и предлагает угадать, кто изображён на фото.

Если не знаете — можно **сдаться и узнать правильный ответ**, а затем перейти к следующему раунду 🎮.

---

## 🖼️ Скриншот (пример UI)

<p align="center">
  <img src="https://github.com/user-attachments/assets/85136a54-b627-4e3e-ab13-b2d76c5b287d" width="380" alt="Game Screenshot" />
</p>

---

## 🧩 Основные фичи

✅ Получение HTML-страницы со списком знаменитостей через `Jsoup`  
✅ Парсинг имени и изображения  
✅ Отображение карточки с фото в стиле **Material 3**  
✅ Ввод имени и проверка результата  
✅ Кнопки **Give up** и **Next**  
✅ Полная поддержка светлой и тёмной темы  
✅ Использует Google Fonts и динамическую цветовую схему Android 12+

---

## 🛠️ Технологический стек

| Категория        | Технологии                                                                                                           |
| ---------------- | -------------------------------------------------------------------------------------------------------------------- |
| 💻 Язык          | ![Kotlin](https://img.shields.io/badge/Kotlin-%237F52FF.svg?logo=kotlin&logoColor=white)                             |
| 🎨 UI            | ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-%2300BCD4.svg?logo=jetpackcompose&logoColor=white) |
| 🎭 Дизайн        | ![Material 3](https://img.shields.io/badge/Material%203-%234285F4.svg?logo=materialdesign&logoColor=white)           |
| 🖼️ Изображения   | ![Coil](https://img.shields.io/badge/Coil-%23FF6F00.svg?logo=kotlin&logoColor=white)                                 |
| 🌐 Парсинг       | ![Jsoup](https://img.shields.io/badge/Jsoup-%23E34F26.svg?logo=java&logoColor=white)                                 |
| ⚙️ Асинхронность | ![Coroutines](https://img.shields.io/badge/Kotlin%20Coroutines-%23844AFF.svg?logo=kotlin&logoColor=white)            |
| 🧩 Типографика   | Google Fonts API (Roboto)                                                                                            |

---

## 📂 Структура проекта

```

app/
├── java/com/example/celebritygame/
│   ├── MainActivity.kt
│   ├── model/
│   │   └── Celebrity.kt
│   ├── network/
│   │   └── CelebrityRepository.kt
│   ├── utils/
│   │   └── HtmlParser.kt
│   └── ui/
│       ├── screens/
│       │   └── CelebrityGameScreen.kt
│       └── theme/
│           ├── Theme.kt
│           ├── Type.kt
│           └── Color.kt
└── res/
├── values/
│   ├── colors.xml
│   └── strings.xml
└── xml/
└── fonts_certs.xml

```

---

## 🚀 Как запустить проект

1. Склонируй репозиторий:
   ```bash
   git clone https://github.com/NotACat1/Guess-The-Celebrity.git
   cd guess-the-celebrity
   ```

2. Открой проект в **Android Studio (Arctic Fox+)**

3. Добавь зависимости в `build.gradle`:

   ```kotlin
   implementation("androidx.compose.material3:material3:1.3.0")
   implementation("io.coil-kt:coil-compose:2.7.0")
   implementation("org.jsoup:jsoup:1.17.2")
   implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
   ```

4. Не забудь добавить в `AndroidManifest.xml`:

   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

5. Запусти 🚀
