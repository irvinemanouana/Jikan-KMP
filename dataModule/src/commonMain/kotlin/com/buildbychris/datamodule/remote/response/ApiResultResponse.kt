package com.buildbychris.datamodule.remote.response

import com.buildbychris.datamodule.remote.response.common.ApiErrorResponseDto


sealed class ApiResultResponse<out T> {
    data class Success<out T>(val data: T) : ApiResultResponse<T>()
    data class Error(val errorResponse: ApiErrorResponseDto) : ApiResultResponse<Nothing>()
}
