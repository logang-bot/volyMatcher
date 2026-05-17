package com.restrusher.volymatcher.ui.screens.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.usecase.GetPlayersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlayersUiState(
    val allPlayers: List<Player> = emptyList(),
    val activeSort: String = "Overall",
) {
    val sorts = listOf("↓ Overall", "Height", "Jump", "Scanned", "+ Filter")
    val sorted: List<Player> = when (activeSort) {
        "Height" -> allPlayers.sortedByDescending { it.height }
        "Jump" -> allPlayers.sortedByDescending { it.jump }
        else -> allPlayers.sortedByDescending { it.skill }
    }
    val mvp: Player? = sorted.firstOrNull()
}

class PlayersViewModel(
    private val getPlayersUseCase: GetPlayersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayersUiState())
    val uiState: StateFlow<PlayersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { _uiState.update { it.copy(allPlayers = getPlayersUseCase()) } }
    }

    fun setSort(sort: String) {
        _uiState.update { it.copy(activeSort = sort.removePrefix("↓ ")) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                PlayersViewModel(GetPlayersUseCase(RepositoryLocator.playerRepository))
            }
        }
    }
}
