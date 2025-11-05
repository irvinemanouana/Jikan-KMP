package com.buildbychris.datamodule.remote.response.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiHeaderResponseDto(
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("has_next_page")
    val hasNextPage: Boolean,
    @SerialName("items")
    val items: ItemsDto,
    @SerialName("last_visible_page")
    val lastVisiblePage: Int
)

@Serializable
data class ItemsDto(
    @SerialName("count")
    val count: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("total")
    val total: Int
)