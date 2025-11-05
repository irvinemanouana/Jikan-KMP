package com.buildbychris.jikan.anime

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val animeModule = module {
    viewModelOf(::AnimeViewModel)
}