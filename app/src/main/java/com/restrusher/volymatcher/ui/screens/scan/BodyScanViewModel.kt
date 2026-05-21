package com.restrusher.volymatcher.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.usecase.GetPlayerByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BodyScanUiState(
    val player: Player? = null,
)

class BodyScanViewModel(
    private val playerId: String?,
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BodyScanUiState())
    val uiState: StateFlow<BodyScanUiState> = _uiState.asStateFlow()

    init {
        if (!playerId.isNullOrBlank()) {
            viewModelScope.launch {
                _uiState.update { it.copy(player = getPlayerByIdUseCase(playerId)) }
            }
        }
    }

    companion object {
        fun factory(playerId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BodyScanViewModel(
                    playerId = playerId,
                    getPlayerByIdUseCase = GetPlayerByIdUseCase(RepositoryLocator.playerRepository),
                )
            }
        }
    }
}
