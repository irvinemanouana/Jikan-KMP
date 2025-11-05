package com.buildbychris.datamodule.di

import com.buildbychris.datamodule.remote.remoteModule
import org.koin.dsl.module

val koinDataModule = module {
    includes(remoteModule)
}