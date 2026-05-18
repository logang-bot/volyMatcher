package com.restrusher.volymatcher.ui.screens.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.BalancedTeams
import com.restrusher.volymatcher.domain.usecase.GetBalancedTeamsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AutoBalanceUiState(
    val balancedTeams: BalancedTeams? = null,
    val skillWeight: Int = 40,
    val heightReachWeight: Int = 25,
    val jumpWeight: Int = 20,
    val speedWeight: Int = 10,
    val roleCoverageWeight: Int = 5,
)

class AutoBalanceViewModel(
    private val getBalancedTeamsUseCase: GetBalancedTeamsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AutoBalanceUiState())
    val uiState: StateFlow<AutoBalanceUiState> = _uiState.asStateFlow()

    init {
        reBalance()
    }

    fun reBalance() {
        viewModelScope.launch {
            _uiState.update { it.copy(balancedTeams = getBalancedTeamsUseCase()) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                AutoBalanceViewModel(
                    GetBalancedTeamsUseCase(RepositoryLocator.playerRepository)
                )
            }
        }
    }
}
