package com.restrusher.volymatcher.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.model.Team
import com.restrusher.volymatcher.domain.usecase.GetPlayersUseCase
import com.restrusher.volymatcher.domain.usecase.GetTeamsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StatsUiState(
    val leaders: List<Player> = emptyList(),
    val teams: List<Team> = emptyList(),
    val activeScope: String = "This month",
    val activeTab: String = "Overall",
) {
    val scopes = listOf("This month", "All time", "This squad")
    val tabs = listOf("Overall", "Highest jump", "Fastest", "Tallest")
}

class StatsViewModel(
    private val getPlayersUseCase: GetPlayersUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() {
        val players = getPlayersUseCase().sortedByDescending { it.skill }
        val teams = getTeamsUseCase().sortedByDescending { it.winPercentage }
        _uiState.update { it.copy(leaders = players.take(6), teams = teams) }
    }

    fun setScope(scope: String) = _uiState.update { it.copy(activeScope = scope) }
    fun setTab(tab: String) = _uiState.update { it.copy(activeTab = tab) }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                StatsViewModel(
                    getPlayersUseCase = GetPlayersUseCase(RepositoryLocator.playerRepository),
                    getTeamsUseCase = GetTeamsUseCase(RepositoryLocator.teamRepository),
                )
            }
        }
    }
}
