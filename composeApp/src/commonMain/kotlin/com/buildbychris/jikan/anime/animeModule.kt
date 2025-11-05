package com.buildbychris.jikan.anime

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val animeModule = module {
    viewModel { AnimeViewModel(get()) }
}