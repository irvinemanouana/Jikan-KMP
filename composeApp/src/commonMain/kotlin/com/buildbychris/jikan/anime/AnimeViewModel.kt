package com.buildbychris.jikan.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildbychris.domain.anime.Anime
import com.buildbychris.domain.anime.AnimeRepository
import com.buildbychris.domain.common.DomainResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AnimeViewModel(
    val animeRepository: AnimeRepository,
    autoLoad: Boolean = true
) : ViewModel() {
    private var _animeListState = MutableStateFlow<AnimeListUiState>(AnimeListUiState.Loading)
    val animeListState = _animeListState.asStateFlow()

    init {
        if (autoLoad) loadAnimeList()
    }

    fun loadAnimeList() {
        viewModelScope.launch {
            animeRepository
                .getAllAnime()
                .onStart {
                    _animeListState.value = AnimeListUiState.Loading
                }.collect { result ->
                    val state = when (result) {
                        is DomainResult.Error -> {
                            AnimeListUiState.Error(result.message)
                        }

                        is DomainResult.Success<List<Anime>> -> {
                            AnimeListUiState.Success(animeList = result.data)
                        }
                    }
                    _animeListState.value = state
                }
        }
    }
}

sealed class AnimeListUiState {
    object Loading : AnimeListUiState()
    data class Success(val animeList: List<Anime>) : AnimeListUiState()
    data class Error(val message: String) : AnimeListUiState()
}