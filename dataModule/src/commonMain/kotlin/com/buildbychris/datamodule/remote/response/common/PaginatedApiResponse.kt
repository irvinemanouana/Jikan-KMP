package com.buildbychris.datamodule.remote.response.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ApiResponseFormat, designed to represent a common structure for API responses that include pagination and a list of data.
 */
@Serializable
data class PaginatedApiResponse<out T>(
    @SerialName("pagination")
    val pagination: ApiHeaderResponseDto,
    @SerialName("data")
    val data: List<T> = emptyList()
)
