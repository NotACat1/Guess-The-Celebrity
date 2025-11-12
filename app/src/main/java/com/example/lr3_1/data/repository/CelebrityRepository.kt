package com.example.lr3_1.data.repository

import com.example.lr3_1.data.model.Celebrity
import com.example.lr3_1.utils.Resource

/**
 * Интерфейс репозитория для получения данных о знаменитостях.
 *
 * Репозиторий инкапсулирует источник данных (например, веб-страницу)
 * и возвращает результат в виде обёртки [Resource], которая отражает
 * текущее состояние операции (успех, ошибка или загрузка).
 */
interface CelebrityRepository {

    /**
     * Загружает список знаменитостей с удалённого источника данных.
     *
     * @return [Resource.Success] с данными, если запрос успешен;
     * [Resource.Error], если произошла ошибка;
     * [Resource.Loading], если данные ещё загружаются.
     */
    suspend fun getCelebrities(): Resource<List<Celebrity>>
}
