package com.buildbychris.domain.anime

data class Anime(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val year: Int?,
    val score: Double,
    val scoreBy: Int
)