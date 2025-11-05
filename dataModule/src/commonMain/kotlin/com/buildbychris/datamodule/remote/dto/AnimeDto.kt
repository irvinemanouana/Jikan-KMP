package com.buildbychris.datamodule.remote.dto

import com.buildbychris.domain.anime.Anime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDto (
    @SerialName("mal_id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("images")
    val imagesUrl: AnimeImagesDto
)

@Serializable
data class AnimeImagesDto (
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("small_image_url")
    val smallImageUrl: String,
    @SerialName("large_image_url")
    val largeImageUrl: String
)

fun AnimeDto.toDomainAnime(): Anime {
    return Anime(
        id = id,
        title = title,
        imageUrl = imagesUrl.imageUrl
    )
}