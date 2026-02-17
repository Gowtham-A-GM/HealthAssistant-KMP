import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.remote.news.NewsApi
import com.example.healthassistant.data.remote.news.dto.NewsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter


class NewsApiImpl(
    private val client: HttpClient,
    private val apiKey: String
) : NewsApi {

    override suspend fun fetchHealthNews(page: Int): NewsResponseDto {

        AppLogger.d("NEWS_API", "Calling STRICT Health NewsAPI")

        AppLogger.d("NEWS_API", "Calling STRICT Health NewsAPI - page $page")

        val response = client.get("https://newsapi.org/v2/everything") {

            parameter(
                "q",
                """
                ("disease outbreak" OR "new disease" OR virus OR infection OR epidemic OR pandemic OR "health alert" OR "WHO alert" OR "medical emergency")
                AND (India OR Indian)
                NOT (sports OR football OR cricket OR movie OR celebrity OR crypto OR AI OR smartphone OR election OR minister OR policy OR tech)
                """.trimIndent()
            )


            parameter("searchIn", "title,description")

            parameter("language", "en")

            parameter("sortBy", "publishedAt")

            parameter("pageSize", 50)

            parameter("page", page)

            parameter("apiKey", apiKey)
        }
            .body<NewsResponseDto>()

        AppLogger.d("NEWS_API", "Page $page -> ${response.articles.size} articles")

        return response
    }


}
