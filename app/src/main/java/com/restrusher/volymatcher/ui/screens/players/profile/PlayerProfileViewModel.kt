package com.restrusher.volymatcher.ui.screens.players.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.usecase.GetMatchesUseCase
import com.restrusher.volymatcher.domain.usecase.GetPlayerByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlayerProfileUiState(
    val player: Player? = null,
    val recentMatches: List<Match> = emptyList(),
)

class PlayerProfileViewModel(
    private val playerId: String,
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase,
    private val getMatchesUseCase: GetMatchesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerProfileUiState())
    val uiState: StateFlow<PlayerProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadData() }
    }

    private fun loadData() {
        _uiState.update {
            it.copy(
                player = getPlayerByIdUseCase(playerId),
                recentMatches = getMatchesUseCase().take(3),
            )
        }
    }

    companion object {
        fun factory(playerId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerProfileViewModel(
                    playerId = playerId,
                    getPlayerByIdUseCase = GetPlayerByIdUseCase(RepositoryLocator.playerRepository),
                    getMatchesUseCase = GetMatchesUseCase(RepositoryLocator.matchRepository),
                )
            }
        }
    }
}
