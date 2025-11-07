package com.buildbychris.datamodule.remote.response.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the pagination metadata returned by the API.
 *
 * This data class contains all the necessary information to handle pagination
 * in API responses, including current page position, navigation capabilities,
 * and item count details.
 */
@Serializable
data class ApiHeaderResponseDto(
    /**
     * The current page number (1-based).
     *
     * @property currentPage The page number currently being viewed
     */
    @SerialName("current_page")
    val currentPage: Int,

    /**
     * Indicates whether there are more pages available after the current page.
     *
     * @property hasNextPage `true` if there is a next page, `false` if this is the last page
     */
    @SerialName("has_next_page")
    val hasNextPage: Boolean,

    /**
     * Contains detailed information about the items in the paginated response.
     *
     * @property items The item count and pagination details
     */
    @SerialName("items")
    val items: ItemsDto,

    /**
     * The highest page number that is currently visible/accessible.
     *
     * This may be different from the total number of pages if the API
     * limits how far ahead users can navigate.
     *
     * @property lastVisiblePage The last page number that can be accessed
     */
    @SerialName("last_visible_page")
    val lastVisiblePage: Int
)

/**
 * Contains detailed information about the items in a paginated response.
 *
 * This data class provides comprehensive item count information including
 * the current page's item count, items per page setting, and total items
 * across all pages.
 */
@Serializable
data class ItemsDto(
    /**
     * The number of items returned in the current page.
     *
     * This may be less than `perPage` on the last page or when there
     * are fewer items available than the page size.
     *
     * @property count The actual number of items in the current page response
     */
    @SerialName("count")
    val count: Int,

    /**
     * The maximum number of items that can be returned per page.
     *
     * This represents the page size setting and is consistent across
     * all pages (except potentially the last page which may have fewer items).
     *
     * @property perPage The configured page size limit
     */
    @SerialName("per_page")
    val perPage: Int,

    /**
     * The total number of items available across all pages.
     *
     * This represents the complete count of items that match the query,
     * regardless of pagination. Use this to calculate total pages:
     * `ceil(total / perPage)`
     *
     * @property total The total count of all available items
     */
    @SerialName("total")
    val total: Int
)