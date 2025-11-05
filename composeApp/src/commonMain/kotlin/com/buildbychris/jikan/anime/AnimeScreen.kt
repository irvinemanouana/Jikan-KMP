package com.buildbychris.jikan.anime

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.buildbychris.domain.anime.Anime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AnimeScreen(
    modifier: Modifier = Modifier,
    state: AnimeListUiState = AnimeListUiState.Loading
) {
    when(state) {
        is AnimeListUiState.Error -> {
            Text(text = state.message)
        }
        AnimeListUiState.Loading -> {}
        is AnimeListUiState.Success -> {

            LazyColumn(modifier = modifier) {
                items(items = state.animeList) {
                    anime ->
                    Text(text = anime.title)
                    HorizontalDivider()
                }

            }
        }
    }
}