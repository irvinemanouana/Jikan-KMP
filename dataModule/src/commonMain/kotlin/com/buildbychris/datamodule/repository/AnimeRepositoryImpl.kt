package com.buildbychris.datamodule.repository

import com.buildbychris.datamodule.remote.dto.AnimeDto
import com.buildbychris.datamodule.remote.response.common.ApiErrorResponseDto
import com.buildbychris.datamodule.remote.response.common.PaginatedApiResponse
import com.buildbychris.datamodule.remote.routes.ApiRoutes
import com.buildbychris.datamodule.remote.dto.toDomainAnime
import com.buildbychris.domain.anime.Anime
import com.buildbychris.domain.anime.AnimeRepository
import com.buildbychris.domain.common.DomainResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnimeRepositoryImpl(val client: HttpClient) : AnimeRepository {
    override fun getAllAnime(): Flow<DomainResult<List<Anime>>> = flow {
        val response = client.get(ApiRoutes.ANIME.path)
        when (response.status.isSuccess()) {
            true -> {
                val body = response.body<PaginatedApiResponse<AnimeDto>>()
                emit(DomainResult.Success(body.data.map { dto ->
                    dto.toDomainAnime()
                }))
            }

            false -> {
                val errorBody = response.body<ApiErrorResponseDto>()
                emit(DomainResult.Error(errorBody.message))
            }
        }
    }

}