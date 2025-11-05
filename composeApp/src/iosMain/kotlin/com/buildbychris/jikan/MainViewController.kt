package com.buildbychris.jikan

import androidx.compose.ui.window.ComposeUIViewController
import com.buildbychris.jikan.di.initKoin

fun MainViewController() = ComposeUIViewController (
    configure = { initKoin {  } }
) { App() }