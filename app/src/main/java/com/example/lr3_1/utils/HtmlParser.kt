package com.example.lr3_1.utils

import com.example.lr3_1.model.Celebrity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * Утилита для парсинга HTML-страницы с информацией о знаменитостях.
 *
 * Извлекает имя и изображение из карточек, расположенных в списке.
 * Использует библиотеку [Jsoup] для обработки HTML.
 *
 * @see Celebrity
 */
object HtmlParser {

    /**
     * Парсит HTML-страницу и возвращает список знаменитостей.
     *
     * Ожидается, что страница содержит элементы в формате IMDb:
     * ```
     * <li class="ipc-metadata-list-summary-item">
     *     <h3 class="ipc-title__text">1. Johnny Depp</h3>
     *     <img class="ipc-image" src="https://..." />
     * </li>
     * ```
     *
     * @param url URL HTML-страницы со списком знаменитостей.
     * @return список объектов [Celebrity], извлечённых из HTML.
     */
    fun parseCelebrities(url: String): List<Celebrity> {
        return try {
            // Загружаем HTML-документ
            val document: Document = Jsoup.connect(url).get()

            // Извлекаем карточки (элементы списка)
            val items: Elements = document.select("li.ipc-metadata-list-summary-item")

            // Преобразуем HTML-элементы в объекты модели
            val celebrities = items.mapNotNull { element ->
                val nameElement = element.selectFirst("h3.ipc-title__text")
                val imageElement = element.selectFirst("img.ipc-image")

                val nameRaw = nameElement?.text()?.trim() ?: return@mapNotNull null
                val imageUrl = imageElement?.attr("src") ?: return@mapNotNull null

                // Удаляем префикс с номером, если он присутствует (например, "1. ")
                val name = nameRaw.replace(Regex("^\\d+\\.\\s*"), "")

                Celebrity(name, imageUrl)
            }

            // Убираем дубликаты (если встречаются одинаковые имена)
            celebrities.distinctBy { it.name }
        } catch (e: Exception) {
            // В случае ошибки парсинга или сети возвращаем пустой список
            e.printStackTrace()
            emptyList()
        }
    }
}
