package com.buildbychris.datamodule.di

import com.buildbychris.datamodule.remote.remoteModule
import com.buildbychris.datamodule.repository.AnimeRepositoryImpl
import com.buildbychris.domain.anime.AnimeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinDataModule = module {
    includes(remoteModule)
    singleOf(::AnimeRepositoryImpl) { bind<AnimeRepository>()}
}