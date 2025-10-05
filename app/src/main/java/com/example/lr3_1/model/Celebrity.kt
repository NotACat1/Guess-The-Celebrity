package com.example.lr3_1.model

/**
 * Domain-модель, представляющая знаменитость.
 *
 * @property name Имя знаменитости, очищенное от лишних символов (например, "1. Johnny Depp" → "Johnny Depp").
 * @property imageUrl Полная ссылка на изображение знаменитости.
 *
 * Используется для отображения карточек в пользовательском интерфейсе.
 */
data class Celebrity(
    val name: String,
    val imageUrl: String
)
