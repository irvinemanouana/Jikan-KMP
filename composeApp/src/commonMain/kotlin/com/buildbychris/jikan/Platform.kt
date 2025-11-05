package com.buildbychris.jikan

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform