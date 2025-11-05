package com.buildbychris.jikan.anime


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.buildbychris.designsystem.component.AnimeCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AnimeScreen(
    modifier: Modifier = Modifier,
    state: AnimeListUiState = AnimeListUiState.Loading
) {
    when (state) {
        is AnimeListUiState.Error -> {
            Text(text = state.message)
        }

        AnimeListUiState.Loading -> {}
        is AnimeListUiState.Success -> {

            LazyVerticalGrid(
                modifier = modifier
                    .padding(8.dp), // marge globale du grid
                columns = GridCells.Adaptive(minSize = 150.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(items = state.animeList) {
                    anime ->
                    AnimeCard(
                        anime = anime
                    )
                }
            }
        }
    }
}