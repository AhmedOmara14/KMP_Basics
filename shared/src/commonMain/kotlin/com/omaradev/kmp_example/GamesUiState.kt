package com.omaradev.kmp_example

import com.omaradev.kmp_example.data.Game


sealed class GamesUiState {
    data object Loading : GamesUiState()
    data class Success(val games: List<Game>) : GamesUiState()
    data class Error(val message: String) : GamesUiState()
}