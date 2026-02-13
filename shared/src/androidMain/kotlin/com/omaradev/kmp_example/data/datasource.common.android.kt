package com.omaradev.kmp_example.data


actual fun provideGameDataSource(): GameRemoteDataSources {
    return AndroidGameDataSource()
}

class AndroidGameDataSource : GameRemoteDataSources {
    private val client = RetrofitClient(baseUrl = baseUrl)
    private val services = client.create<GameServices>()

    override suspend fun getGames(): List<Game> {
        return services.getGames()
    }

    companion object {
        private const val baseUrl = "https://www.freetogame.com/api/"
    }
}