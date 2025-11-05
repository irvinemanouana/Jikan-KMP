package com.buildbychris.jikan.di

import com.buildbychris.datamodule.di.koinDataModule
import com.buildbychris.jikan.anime.animeModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes Koin dependency injection.
 * This function starts the Koin container and loads the necessary modules.
 *
 * @param config An optional lambda for additional Koin configuration.
 */
fun initKoin(config: KoinAppDeclaration?= null) {
    startKoin {
        config?.invoke(this)
        modules(
            koinDataModule,
            animeModule
        )
    }
}