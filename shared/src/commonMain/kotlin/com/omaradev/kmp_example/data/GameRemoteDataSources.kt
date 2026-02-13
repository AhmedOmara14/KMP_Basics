package com.omaradev.kmp_example.data

interface GameRemoteDataSources {
    suspend fun getGames(): List<Game>
}