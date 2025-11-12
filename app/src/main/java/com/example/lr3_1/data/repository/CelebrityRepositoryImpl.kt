package com.example.lr3_1.data.repository

import com.example.lr3_1.data.model.Celebrity
import com.example.lr3_1.utils.Resource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

/**
 * Реализация [CelebrityRepository], использующая [HttpClient] Ktor
 * для загрузки HTML-страницы и [Jsoup] для парсинга HTML-документа.
 *
 * Извлекает информацию об актёрах с сайта [https://www.onthisday.com/people/actors].
 *
 * @constructor Внедряется через Hilt с помощью [HttpClient].
 * @param httpClient HTTP-клиент для выполнения сетевых запросов.
 */
class CelebrityRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : CelebrityRepository {

    companion object {
        /** URL страницы со списком актёров. */
        private const val ACTORS_URL = "https://www.onthisday.com/people/actors"

        /** Базовый URL сайта для формирования абсолютных ссылок. */
        private const val BASE_URL = "https://www.onthisday.com"
    }

    /**
     * Загружает и парсит HTML-страницу со списком актёров.
     *
     * Выполняет сетевой запрос на [ACTORS_URL], парсит HTML через [Jsoup],
     * и извлекает список актёров из элементов `ul.photo-list.photo-list--full-width li`.
     *
     * @return [Resource.Success] с готовым списком актёров,
     * либо [Resource.Error], если загрузка или парсинг не удались.
     */
    override suspend fun getCelebrities(): Resource<List<Celebrity>> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Загрузка HTML-страницы
                val html = httpClient.get(ACTORS_URL).bodyAsText()
                val doc = Jsoup.parse(html)

                // 2. Извлечение карточек актёров
                val listItems = doc.select("ul.photo-list.photo-list--full-width li")

                if (listItems.isEmpty()) {
                    return@withContext Resource.Error(
                        "Could not find list items. The site structure may have changed."
                    )
                }

                // 3. Парсинг каждой карточки актёра
                val celebrities = listItems.mapNotNull { item ->
                    try {
                        val linkElement = item.selectFirst("a")
                        val imgElement = item.selectFirst("img")

                        val relativeUrl = linkElement?.attr("href")?.trim()
                        val id = relativeUrl?.substringAfterLast("/")

                        val name = linkElement?.ownText()?.trim()

                        val smallImageUrl = imgElement?.attr("src")
                        val largeImageUrl = imgElement?.attr("srcset")
                            ?.split(" ")
                            ?.firstOrNull()
                            ?.let { "$BASE_URL$it" }
                            ?: smallImageUrl?.let { "$BASE_URL$it" }

                        if (!id.isNullOrBlank() && !name.isNullOrBlank() && !largeImageUrl.isNullOrBlank()) {
                            Celebrity(id, name, largeImageUrl)
                        } else null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }

                if (celebrities.isEmpty()) {
                    Resource.Error("Actors could not be extracted.")
                } else {
                    Resource.Success(celebrities)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error("Page loading error: ${e.message}")
            }
        }
    }
}
