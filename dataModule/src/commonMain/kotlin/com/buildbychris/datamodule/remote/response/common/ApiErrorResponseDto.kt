package com.buildbychris.datamodule.remote.response.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponseDto(
    @SerialName("error")
    val error: String,
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: Int
)