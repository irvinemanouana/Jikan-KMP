package com.buildbychris.domain.anime

import com.buildbychris.domain.common.DomainResult
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun getAllAnime(): Flow<DomainResult<List<Anime>>>
}