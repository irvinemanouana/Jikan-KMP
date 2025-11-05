package com.buildbychris.datamodule.remote.dto

import com.buildbychris.domain.anime.Anime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDto(
    @SerialName("mal_id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("images")
    val images: AnimeImagesDto
)

@Serializable
data class AnimeImagesDto(
    @SerialName("jpg")
    val jpg: Jpeg,
    @SerialName("webp")
    val webp: Webp
)


@Serializable
data class Jpeg(
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("small_image_url")
    val smallImageUrl: String,
    @SerialName("large_image_url")
    val largeImageUrl: String
)

@Serializable
data class Webp(
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
        imageUrl = images.webp.imageUrl

    )
}