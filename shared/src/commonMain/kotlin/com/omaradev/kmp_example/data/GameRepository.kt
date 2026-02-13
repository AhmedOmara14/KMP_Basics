package com.omaradev.kmp_example.data

class GameRepository {
    private val dataSources: GameRemoteDataSources = provideGameDataSource()

    // Constructor للـ iOS (بدون parameters)
    constructor()

    suspend fun getGames(): List<Game> {
        return dataSources.getGames()
    }
}

