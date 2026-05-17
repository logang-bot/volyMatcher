package com.restrusher.volymatcher.ui.screens.teams.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Team
import com.restrusher.volymatcher.domain.usecase.GetTeamByNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeamDetailUiState(
    val team: Team? = null,
)

class TeamDetailViewModel(
    private val teamName: String,
    private val getTeamByNameUseCase: GetTeamByNameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamDetailUiState())
    val uiState: StateFlow<TeamDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(team = getTeamByNameUseCase(teamName)) }
        }
    }

    companion object {
        fun factory(teamName: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TeamDetailViewModel(
                    teamName = teamName,
                    getTeamByNameUseCase = GetTeamByNameUseCase(RepositoryLocator.teamRepository),
                )
            }
        }
    }
}
