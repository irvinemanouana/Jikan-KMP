package com.buildbychris.datamodule.remote


import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val remoteModule: Module = module {
    single { HttpClientFactory.getHttpClientInstance(engine = OkHttp.create()) }
}