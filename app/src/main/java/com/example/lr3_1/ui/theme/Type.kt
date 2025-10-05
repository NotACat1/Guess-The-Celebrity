package com.example.lr3_1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.example.lr3_1.R

// ---------------------------------------------
// Провайдер шрифтов Google Fonts
// ---------------------------------------------
// Указывает системе, что шрифты должны загружаться через Google Play Services
// и проверяться по предоставленным сертификатам
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// ---------------------------------------------
// Наборы шрифтов для различных стилей текста
// ---------------------------------------------

// Шрифт для основных текстовых блоков (body)
val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"), // используем шрифт Roboto
        fontProvider = provider,           // загружаем через указанный провайдер
    )
)

// Шрифт для крупных заголовков (display, headline)
val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
    )
)

// ---------------------------------------------
// Базовая типографика Material 3
// ---------------------------------------------
// Содержит стандартные стили текста, которые мы можем переопределять
val baseline = Typography()

// ---------------------------------------------
// Кастомная типографика приложения
// ---------------------------------------------
// Создаём собственный объект Typography, в котором переопределяем семейство шрифтов
// для различных стилей (display, headline, body, label).
// Это обеспечивает единообразный стиль текста во всём приложении.
val AppTypography = Typography(
    // Display (крупные заголовки и титры)
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),

    // Headline (заголовки разделов)
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),

    // Title (подзаголовки)
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),

    // Body (основной текст)
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),

    // Label (подписи, элементы управления)
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)