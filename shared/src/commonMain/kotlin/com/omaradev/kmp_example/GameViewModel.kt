package com.omaradev.kmp_example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omaradev.kmp_example.data.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: GameRepository = GameRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = GamesUiState.Loading
            try {
                _uiState.value = GamesUiState.Success( repository.getGames())
            }catch (e: Exception){
                _uiState.value = GamesUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun retry() {
        loadGames()
    }
}