package com.buildbychris.jikan.anime

import com.buildbychris.domain.anime.Anime
import com.buildbychris.domain.anime.AnimeRepository
import com.buildbychris.domain.common.DomainResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue


/**
 * Unit tests for the [AnimeViewModel].
 * This class verifies the state management logic based on different results from the AnimeRepository.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AnimeViewModelTest : KoinTest {
    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var fakeRepository: FakeAnimeRepository

    private val  testDispatcher = StandardTestDispatcher()

    private val testAnimeModule = module {
        singleOf(::FakeAnimeRepository) { bind<AnimeRepository>()}
        viewModel { AnimeViewModel(get(), false) }
    }

    @BeforeTest
    fun setup() {
        startKoin {
            modules(testAnimeModule)
        }
        fakeRepository = get()
        animeViewModel = get()

        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `loadAnimeList WHEN repository returns Success THEN state is Success`() = runTest(testDispatcher) {
        // Given
        fakeRepository.shouldReturnError = false
        fakeRepository.shouldReturnEmpty = false

        // When
        animeViewModel.loadAnimeList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = animeViewModel.animeListState.value
        assertTrue(finalState is AnimeListUiState.Success)
        assertTrue(finalState.animeList.isNotEmpty())
    }

    // ✅ 2. Error case
    @Test
    fun `loadAnimeList WHEN repository returns Error THEN state is Error`() = runTest(testDispatcher) {
        // Given
        fakeRepository.shouldReturnError = true

        // When
        animeViewModel.loadAnimeList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = animeViewModel.animeListState.value
        assertTrue(finalState is AnimeListUiState.Error)
        //assertTrue(finalState.message == "Failed to fetch anime")
    }

    // 3. Empty list case
    @Test
    fun `loadAnimeList WHEN repository returns empty list THEN state is Success but empty`() = runTest(testDispatcher) {
        // Given
        fakeRepository.shouldReturnEmpty = true

        // When
        animeViewModel.loadAnimeList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = animeViewModel.animeListState.value
        assertTrue(finalState is AnimeListUiState.Success)
        assertTrue(finalState.animeList.isEmpty())
    }

    // ✅ 4. Initial state
    @Test
    fun `initial state SHOULD be Loading`() = runTest {
        val initialState = animeViewModel.animeListState.value
        assertTrue(initialState is AnimeListUiState.Loading)
    }


    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }
}

class FakeAnimeRepository: AnimeRepository {
    var shouldReturnError = false
    var shouldReturnEmpty = false

    override fun getAllAnime(): Flow<DomainResult<List<Anime>>> = flow {
        delay(50) // Simulate async operation

        when {
            shouldReturnError -> emit(DomainResult.Error("Failed to fetch anime"))
            shouldReturnEmpty -> emit(DomainResult.Success(emptyList()))
            else -> emit(DomainResult.Success(listOf(Anime(
                id = 5114,
                title = "Fullmetal Alchemist: Brotherhood",
                imageUrl = "https://cdn.myanimelist.net/images/anime/1223/96541.jpg",
                year = 2009,
                score = 9.10,
                scoreBy = 2101990
            ))))
        }
    }
}