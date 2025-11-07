package com.buildbychris.jikan.anime


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.buildbychris.designsystem.component.AnimeCard
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AnimeScreen(
    modifier: Modifier = Modifier,
    state: AnimeState = AnimeState(),
    contentPadding: PaddingValues,
    onLoadAnime: () -> Unit,
) {
    val animeLazyGridState = rememberLazyGridState()

    LaunchedEffect(state.animeList) {
        snapshotFlow {
            animeLazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.distinctUntilChanged()
            .collect {
                lastVisibleItemIndex ->
                if (lastVisibleItemIndex == state.animeList.lastIndex) {
                    // Load more items here
                    onLoadAnime()
                }

            }
    }
    LazyVerticalGrid(
        state = animeLazyGridState,
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items = state.animeList) { anime ->
            AnimeCard(
                anime = anime
            )
        }
        if (state.isLoading) {
            item {
                Text(text = "Loading...")
            }
        }
    }

    /*when (state) {
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
    }*/

}