package com.omaradev.kmp_example.data

import retrofit2.http.GET

interface GameServices {
    @GET("games")
    suspend fun getGames(): List<Game>
}