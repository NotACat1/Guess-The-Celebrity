package com.example.lr3_1.network

import com.example.lr3_1.model.Celebrity
import com.example.lr3_1.utils.HtmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Репозиторий, отвечающий за получение и кэширование данных о знаменитостях.
 *
 * Изолирует логику доступа к данным от UI-слоя, реализуя паттерн Repository.
 * Использует HTML-парсер для извлечения списка знаменитостей с удалённой страницы.
 *
 * Работает асинхронно, переключая контекст в [Dispatchers.IO].
 */
object CelebrityRepository {

    /**
     * URL страницы IMDb (или аналогичного сайта), содержащей список знаменитостей.
     * Можно заменить на другой источник при необходимости.
     */
    private const val CELEBRITY_URL = "https://www.imdb.com/list/ls052283250/"

    /**
     * Загружает HTML-документ и парсит его в список [Celebrity].
     *
     * Выполняется в IO-потоке, чтобы не блокировать основной поток.
     *
     * @return список знаменитостей с именами и ссылками на изображения.
     */
    suspend fun loadCelebrities(): List<Celebrity> = withContext(Dispatchers.IO) {
        HtmlParser.parseCelebrities(CELEBRITY_URL)
    }
}
