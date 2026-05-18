package com.restrusher.volymatcher.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.usecase.GetMatchesUseCase
import com.restrusher.volymatcher.domain.usecase.GetPlayersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val nextMatch: Match? = null,
    val recentMatches: List<Match> = emptyList(),
    val squadPlayers: List<Player> = emptyList(),
)

class HomeViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() {
        val matches = getMatchesUseCase()
        val players = getPlayersUseCase()
        _uiState.update {
            it.copy(
                nextMatch = matches.firstOrNull(),
                recentMatches = matches.drop(1).take(2),
                squadPlayers = players,
            )
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    getMatchesUseCase = GetMatchesUseCase(RepositoryLocator.matchRepository),
                    getPlayersUseCase = GetPlayersUseCase(RepositoryLocator.playerRepository),
                )
            }
        }
    }
}
