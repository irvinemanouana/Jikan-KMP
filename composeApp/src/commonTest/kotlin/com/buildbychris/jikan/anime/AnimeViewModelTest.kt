package com.buildbychris.jikan.anime

import com.buildbychris.datamodule.remote.dto.AnimeDto
import com.buildbychris.datamodule.remote.dto.toDomainAnime
import com.buildbychris.datamodule.remote.response.common.PaginatedApiResponse
import com.buildbychris.datamodule.remote.routes.ApiRoutes
import com.buildbychris.domain.anime.AnimePage
import com.buildbychris.domain.anime.AnimeRepository
import com.buildbychris.domain.utils.Paginator
import com.buildbychris.jikan.network.MockHttpClientFactory.createMockHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue


/**
 * Unit tests for the [AnimeViewModel].
 * This class verifies the state management logic based on different results from the AnimeRepository.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AnimeViewModelTest : KoinTest {
    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var fakeRepository: FakeAnimeRepository

    // Use a more realistic JSON structure that matches AnimePage
    val jsonResponse = """
            {
                "pagination": { "last_visible_page": 5, "has_next_page": true, "current_page": 1 },
                "data": [
                    { "mal_id": 1, "title": "Naruto", "images": { "jpg": { "image_url": "url1", "small_image_url": "url1", "large_image_url": "url1" } }, "year": 2002, "score": 7.9, "scored_by": 100000 },
                    { "mal_id": 2, "title": "One Piece", "images": { "jpg": { "image_url": "url2", "small_image_url": "url2", "large_image_url": "url2" } }, "year": 1999, "score": 8.5, "scored_by": 200000 }
                ]
            }
        """

    private val  testDispatcher = StandardTestDispatcher()

    private val testAnimeModule = module {
        single { createMockHttpClient(
            jsonResponse,
            HttpStatusCode.OK
        )
        }
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
    fun `return success result from the API`() = runTest(testDispatcher) {
        // Given
        var loadingState = false
        var successResult: AnimePage? = null
        var errorResult: Throwable? = null

        val paginator = fakeRepository.getAnimePages(
            onLoadUpdated = { isLoading -> loadingState = isLoading },
            onError = { error -> errorResult = error },
            onSuccess = { animePage -> successResult = animePage }
        )


        // When
        paginator.loadNextItems()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = animeViewModel.state.value
        assertFalse(loadingState)
        assertNull(errorResult)
        assertTrue(successResult!!.animeList.isNotEmpty())
        assertTrue(finalState.animeList.isEmpty())
    }

    @Test
    fun `Should return empty list of anime`() = runTest(testDispatcher) {
        // Given
        fakeRepository.shouldReturnEmpty = true

        var loadingState = false
        var successResult: AnimePage? = null
        var errorResult: Throwable? = null

        val paginator = fakeRepository.getAnimePages(
            onLoadUpdated = { isLoading -> loadingState = isLoading },
            onError = { error -> errorResult = error },
            onSuccess = { animePage -> successResult = animePage }
        )


        // When
        paginator.loadNextItems()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = animeViewModel.state.value
        assertFalse(loadingState)
        assertNull(errorResult)
        assertTrue(successResult!!.animeList.isEmpty())
        assertTrue(finalState.animeList.isEmpty())
    }

    @Test
    fun `Should return error`() = runTest(testDispatcher) {
        // Given
        fakeRepository.shouldReturnError = true

        var loadingState = false
        var successResult: AnimePage? = null
        var errorResult: Throwable? = null

        val paginator = fakeRepository.getAnimePages(
            onLoadUpdated = { isLoading -> loadingState = isLoading },
            onError = { error -> errorResult = error },
            onSuccess = { animePage -> successResult = animePage }
        )


        // When
        paginator.loadNextItems()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = animeViewModel.state.value
        assertFalse(loadingState)
        assertTrue(errorResult != null)
        assertNull(successResult)
        assertTrue(finalState.animeList.isEmpty())
    }



    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }
}

class FakeAnimeRepository(val mockClient: HttpClient): AnimeRepository {


    var shouldReturnError = false
    var shouldReturnEmpty = false
    override fun getAnimePages(
        onLoadUpdated: (Boolean) -> Unit,
        onError: suspend (Throwable?) -> Unit,
        onSuccess: suspend (AnimePage) -> Unit
    ): Paginator<Int, AnimePage> {
        return Paginator(
            initialKey = 1,
            onLoadUpdated = onLoadUpdated,
            onSuccess =  {
                result, currentKey ->
                onSuccess(result)
            },
            onError = onError,
            onRequest = {
                currentKey ->
                when {
                    shouldReturnError -> Result.failure(Throwable("Error"))
                    shouldReturnEmpty -> Result.success(AnimePage(1, false, emptyList()))
                    else -> {
                        val request = mockClient.get(ApiRoutes.ANIME.path)
                        val body = request.body<PaginatedApiResponse<AnimeDto>>()

                        val animePage = AnimePage(
                            pageId = body.pagination.currentPage,
                            hasNextPage = body.pagination.hasNextPage,
                            animeList = body.data.map {
                                    dto ->
                                dto.toDomainAnime()
                            }
                        )

                        Result.success(animePage)
                    }
                }
            },
            getNextKey = { currentKey, result ->
                currentKey + 1

            },
            endReached = { currentKey, result ->
                !result.hasNextPage
            }
        )
    }
}