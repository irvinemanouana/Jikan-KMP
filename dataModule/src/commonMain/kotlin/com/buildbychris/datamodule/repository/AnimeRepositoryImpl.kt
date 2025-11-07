package com.buildbychris.datamodule.repository

import com.buildbychris.datamodule.remote.dto.AnimeDto
import com.buildbychris.datamodule.remote.dto.toDomainAnime
import com.buildbychris.datamodule.remote.response.common.ApiErrorResponseDto
import com.buildbychris.datamodule.remote.response.common.PaginatedApiResponse
import com.buildbychris.datamodule.remote.routes.ApiRoutes
import com.buildbychris.domain.anime.AnimePage
import com.buildbychris.domain.anime.AnimeRepository
import com.buildbychris.domain.utils.Paginator
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

class AnimeRepositoryImpl(val client: HttpClient) : AnimeRepository {

    override fun getAnimePages(
        onLoadUpdated: (Boolean) -> Unit,
        onError: suspend (Throwable?) -> Unit,
        onSuccess: suspend (result: AnimePage) -> Unit,
    ): Paginator<Int, AnimePage> {
        val paginator = Paginator(
            initialKey = 1,
            onLoadUpdated = onLoadUpdated,
            onRequest = { nextPage ->
                val response = client.get(
                    urlString = ApiRoutes.ANIME.path) {
                    parameter("page", nextPage)
                }
                if (response.status.isSuccess()) {
                    val body = response.body<PaginatedApiResponse<AnimeDto>>()
                    val animePage = AnimePage(
                        pageId = body.pagination.currentPage,
                        hasNextPage = body.pagination.hasNextPage,
                        animeList = body.data.map {
                            dto ->
                            dto.toDomainAnime()
                        }
                    )
                    Result.success(animePage)
                } else {
                    val error = response.body<ApiErrorResponseDto>()
                    Result.failure(Exception(error.message))
                }

            },
            getNextKey = { currentKey, result ->
                currentKey + 1

            },
            onError = onError,
            onSuccess = {
                result, _ ->
                onSuccess(result)
            },
            endReached = { currentKey, result ->
                !result.hasNextPage
            }
        )
        return paginator
    }

}