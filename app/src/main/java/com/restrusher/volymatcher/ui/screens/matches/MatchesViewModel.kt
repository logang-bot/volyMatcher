package com.restrusher.volymatcher.ui.screens.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.MatchFormat
import com.restrusher.volymatcher.domain.usecase.GetMatchesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MatchesUiState(
    val allMatches: List<Match> = emptyList(),
    val activeFilter: String = "All",
) {
    val filters = listOf("All", "Volleyball", "Soccer", "Basketball", "Battle Royale")
    val filteredMatches: List<Match> = when (activeFilter) {
        "All" -> allMatches
        "Battle Royale" -> allMatches.filter { it.format == MatchFormat.BattleRoyale }
        else -> allMatches.filter { it.sport.equals(activeFilter, ignoreCase = true) }
    }
}

class MatchesViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { _uiState.update { it.copy(allMatches = getMatchesUseCase()) } }
    }

    fun setFilter(filter: String) {
        _uiState.update { it.copy(activeFilter = filter) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                MatchesViewModel(GetMatchesUseCase(RepositoryLocator.matchRepository))
            }
        }
    }
}
