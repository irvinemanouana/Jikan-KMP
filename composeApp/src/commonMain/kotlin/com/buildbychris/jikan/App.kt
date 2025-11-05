package com.buildbychris.jikan

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buildbychris.jikan.anime.AnimeScreen
import com.buildbychris.jikan.anime.AnimeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val animeViewModel = koinViewModel<AnimeViewModel>()
        val animeState by animeViewModel.animeListState.collectAsStateWithLifecycle()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            paddingValues ->
            AnimeScreen(
                modifier = Modifier.padding(paddingValues),
                state = animeState
            )
        }

    }
}