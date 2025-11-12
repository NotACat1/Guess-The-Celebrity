package com.example.lr3_1.data.model

/**
 * Модель данных, представляющая знаменитость (актёра).
 *
 * Используется для отображения информации, полученной
 * с сайта [https://www.onthisday.com/people/actors].
 *
 * @property id Уникальный идентификатор знаменитости, обычно извлекается из URL.
 * @property name Имя актёра.
 * @property imageUrl Полный URL изображения актёра.
 */
data class Celebrity(
    val id: String,
    val name: String,
    val imageUrl: String
)
