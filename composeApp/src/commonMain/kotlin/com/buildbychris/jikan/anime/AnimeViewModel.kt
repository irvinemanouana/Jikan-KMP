package com.buildbychris.jikan.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildbychris.domain.anime.Anime
import com.buildbychris.domain.anime.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimeViewModel(
    animeRepository: AnimeRepository,
    autoLoad: Boolean = true
) : ViewModel() {

    private val _state = MutableStateFlow(AnimeState())
    val state = _state.asStateFlow()

    val animePager = animeRepository.getAnimePages(
        onLoadUpdated = { isLoading ->
            _state.update {
                it.copy(isLoading = isLoading)
            }
        },
        onError = { error ->
            _state.update {
                it.copy(
                    error = error?.message,
                )
            }
        },
        onSuccess = { result ->
            _state.update {
                it.copy(
                    animeList = it.animeList + result.animeList,
                    error = null,
                )
            }
        }
    )


    init {
        if (autoLoad) loadAnimeList()
    }

    fun loadAnimeList() {
        viewModelScope.launch {
            animePager.loadNextItems()
        }
    }
}

data class AnimeState(
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)