package com.restrusher.volymatcher.ui.screens.matches.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.usecase.GetMatchByIdUseCase
import com.restrusher.volymatcher.domain.usecase.GetPlayersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MatchDetailUiState(
    val match: Match? = null,
    val teamAPlayers: List<Player> = emptyList(),
    val teamBPlayers: List<Player> = emptyList(),
)

class MatchDetailViewModel(
    private val matchId: String,
    private val getMatchByIdUseCase: GetMatchByIdUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchDetailUiState())
    val uiState: StateFlow<MatchDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() {
        val match = getMatchByIdUseCase(matchId)
        val players = getPlayersUseCase()
        _uiState.update {
            it.copy(
                match = match,
                teamAPlayers = players.take(3),
                teamBPlayers = players.drop(6).take(3),
            )
        }
    }

    companion object {
        fun factory(matchId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MatchDetailViewModel(
                    matchId = matchId,
                    getMatchByIdUseCase = GetMatchByIdUseCase(RepositoryLocator.matchRepository),
                    getPlayersUseCase = GetPlayersUseCase(RepositoryLocator.playerRepository),
                )
            }
        }
    }
}
