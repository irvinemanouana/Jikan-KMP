package com.buildbychris.domain.anime

data class AnimePage(
    val pageId: Int,
    val hasNextPage: Boolean = true,
    val animeList: List<Anime> = emptyList()
)
