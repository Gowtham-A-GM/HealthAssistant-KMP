package com.example.healthassistant.data.remote.news

import com.example.healthassistant.data.remote.news.dto.NewsResponseDto

interface NewsApi {
    suspend fun fetchHealthNews(page: Int = 1): NewsResponseDto

}
