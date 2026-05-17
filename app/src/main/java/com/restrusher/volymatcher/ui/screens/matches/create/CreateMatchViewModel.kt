package com.restrusher.volymatcher.ui.screens.matches.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.restrusher.volymatcher.domain.model.MatchFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateMatchUiState(
    val selectedFormat: MatchFormat = MatchFormat.Official,
    val selectedBrStyle: String = "King of the court",
    val selectedSport: String = "Volleyball",
    val playersPerTeam: Int = 3,
)

class CreateMatchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateMatchUiState())
    val uiState: StateFlow<CreateMatchUiState> = _uiState.asStateFlow()

    fun setFormat(format: MatchFormat) = _uiState.update { it.copy(selectedFormat = format) }
    fun setBrStyle(style: String) = _uiState.update { it.copy(selectedBrStyle = style) }
    fun setSport(sport: String) = _uiState.update { it.copy(selectedSport = sport) }
    fun setPlayersPerTeam(count: Int) = _uiState.update { it.copy(playersPerTeam = count) }

    companion object {
        val Factory = viewModelFactory { initializer { CreateMatchViewModel() } }
    }
}
