package com.buildbychris.domain.anime

import com.buildbychris.domain.common.DomainResult
import com.buildbychris.domain.utils.Paginator
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun getAnimePages(
        onLoadUpdated: (Boolean) -> Unit,
        onError: suspend (Throwable?) -> Unit,
        onSuccess: suspend (result: AnimePage) -> Unit,
    ): Paginator<Int, AnimePage>
}