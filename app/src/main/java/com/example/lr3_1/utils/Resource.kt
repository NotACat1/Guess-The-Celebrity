package com.example.lr3_1.utils

/**
 * Универсальный класс-обёртка для представления состояния загрузки данных.
 *
 * Используется для унификации работы с асинхронными результатами.
 *
 * @param T Тип данных, возвращаемых при успешной загрузке.
 * @property data Содержимое результата (если успешно).
 * @property message Сообщение об ошибке (если есть).
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /** Успешный результат. */
    class Success<T>(data: T) : Resource<T>(data)

    /** Ошибка при выполнении операции. */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /** Состояние загрузки. */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
