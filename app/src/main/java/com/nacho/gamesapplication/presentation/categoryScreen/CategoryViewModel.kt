package com.nacho.gamesapplication.presentation.categoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.domain.repository.IGamesRepository
import com.nacho.gamesapplication.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: IGamesRepository,
) : ViewModel() {

    private var _categoryScreenUiState = MutableStateFlow(CategoryStates())
    val categoryScreenUiState = _categoryScreenUiState.asStateFlow()

    fun getGames(category: String) {
        viewModelScope.launch {
            _categoryScreenUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }
            repository.getGameByCategory(category).onEach { result ->
                when (result) {

                    is Resources.Success -> {
                        delay(500)
                        val games = result.data ?: emptyList()
                        _categoryScreenUiState.update {
                            it.copy(
                                isLoading = false,
                                games = games,
                            )
                        }
                    }

                    is Resources.Error -> {
                        _categoryScreenUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message,
                            )
                        }
                    }
                }

            }.launchIn(this)
        }
    }

}

data class CategoryStates(
    val isLoading: Boolean = false,
    val games: List<Games> = emptyList(),
    val errorMessage: String? = null,
)