package com.restrusher.volymatcher.ui.screens.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Team
import com.restrusher.volymatcher.domain.usecase.GetPlayersUseCase
import com.restrusher.volymatcher.domain.usecase.GetTeamsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeamsUiState(
    val teams: List<Team> = emptyList(),
    val totalPlayerCount: Int = 0,
)

class TeamsViewModel(
    private val getTeamsUseCase: GetTeamsUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamsUiState())
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teams = getTeamsUseCase(),
                    totalPlayerCount = getPlayersUseCase().size,
                )
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                TeamsViewModel(
                    getTeamsUseCase = GetTeamsUseCase(RepositoryLocator.teamRepository),
                    getPlayersUseCase = GetPlayersUseCase(RepositoryLocator.playerRepository),
                )
            }
        }
    }
}
