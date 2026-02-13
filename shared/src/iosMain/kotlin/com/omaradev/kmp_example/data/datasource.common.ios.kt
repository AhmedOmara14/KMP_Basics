package com.omaradev.kmp_example.data

import kotlinx.serialization.json.Json

actual fun provideGameDataSource(): GameRemoteDataSources {
    return IOSGameDataSource()
}

class IOSGameDataSource : GameRemoteDataSources {
    private val client = URLSessionClient()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getGames(): List<Game> {
        val jsonString = client.fetch("$baseUrl/games")
        return json.decodeFromString<List<Game>>(jsonString)
    }

    companion object {
        private const val baseUrl = "https://www.freetogame.com/api"
    }
}