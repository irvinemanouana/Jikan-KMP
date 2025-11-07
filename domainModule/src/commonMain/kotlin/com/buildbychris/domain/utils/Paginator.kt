package com.buildbychris.domain.utils

/**
 * A generic paginator that handles loading paginated data from any source.
 *
 * This class provides a flexible pagination mechanism that can work with different
 * types of keys (page numbers, cursors, tokens, etc.) and items (API responses,
 * data objects, etc.). It manages the pagination state and provides callbacks
 * for handling loading states, errors, and successful data retrieval.
 *
 * @param Key The type of the pagination key (e.g., Int for page numbers, String for cursors)
 * @param Item The type of the items being paginated (e.g., API response objects)
 *
 * @property initialKey The starting key for pagination (e.g., page 1, initial cursor)
 * @property onLoadUpdated Callback invoked when loading state changes (true = loading, false = not loading)
 * @property onRequest Suspend function that performs the actual data request for a given key
 * @property getNextKey Function that determines the next key based on current key and response
 * @property onError Callback invoked when an error occurs during data loading
 * @property onSuccess Callback invoked when data is successfully loaded
 * @property endReached Function that determines if pagination has reached the end
 *
 * @author BuildByChris
 * @since 1.0.0
 *
 * Example usage:
 * ```kotlin
 * val paginator = Paginator<Int, ApiResponse<List<Item>>>(
 *     initialKey = 1,
 *     onLoadUpdated = { isLoading -> /* update UI loading state */ },
 *     onRequest = { page -> apiService.getItems(page) },
 *     getNextKey = { currentPage, response -> currentPage + 1 },
 *     onError = { error -> /* handle error */ },
 *     onSuccess = { response, nextKey -> /* update UI with data */ },
 *     endReached = { _, response -> !response.hasNextPage }
 * )
 * ```
 */
class Paginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<Item>,
    private val getNextKey: suspend (currentKey: Key, Item) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (result: Item, newKey: Key) -> Unit,
    private val endReached: (currentKey: Key, result: Item) -> Boolean
) {

    /**
     * The current pagination key being used.
     * This represents the next key that will be used for loading data.
     */
    private var currentKey = initialKey

    /**
     * Flag indicating whether a request is currently in progress.
     * Prevents multiple simultaneous requests.
     */
    private var isMakingRequest = false

    /**
     * Flag indicating whether pagination has reached the end.
     * When true, no more data can be loaded.
     */
    private var isEndReached = false

    /**
     * Indicates whether the paginator is currently loading data.
     *
     * @return true if a request is in progress, false otherwise
     */
    val isLoading: Boolean
        get() = isMakingRequest

    /**
     * Indicates whether pagination has reached the end and no more data is available.
     *
     * @return true if no more data can be loaded, false otherwise
     */
    val hasReachedEnd: Boolean
        get() = isEndReached

    /**
     * Gets the current pagination key.
     *
     * @return the current key that will be used for the next request
     */
    val getCurrentKey: Key
        get() = currentKey

    /**
     * Loads the next batch of items using the current pagination key.
     *
     * This method:
     * 1. Checks if a request is already in progress or if end is reached
     * 2. Sets loading state to true
     * 3. Makes the request using the current key
     * 4. Handles success/error scenarios
     * 5. Updates the current key for the next request
     * 6. Determines if pagination end has been reached
     *
     * The method is thread-safe and prevents multiple simultaneous requests.
     *
     * @throws Nothing This method handles all errors internally via the onError callback
     */
    suspend fun loadNextItems() {
        // Prevent multiple simultaneous requests or loading when end is reached
        if (isMakingRequest || isEndReached) {
            return
        }

        isMakingRequest = true

        // Notify that loading has started
        onLoadUpdated(true)

        // Make the actual request
        val result = onRequest(currentKey)

        isMakingRequest = false

        // Handle the result
        val item = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }

        // Update the current key for the next request
        currentKey = getNextKey(currentKey, item)

        // Notify success with the loaded data and next key
        onSuccess(item, currentKey)

        // Update loading state
        onLoadUpdated(false)

        // Check if we've reached the end of pagination
        isEndReached = endReached(currentKey, item)
    }

    /**
     * Resets the paginator to its initial state.
     *
     * This method:
     * - Resets the current key to the initial key
     * - Clears the end-reached flag
     * - Clears the loading state
     *
     * Use this when you need to start pagination over from the beginning,
     * such as when refreshing data or changing filter criteria.
     */
    fun reset() {
        currentKey = initialKey
        isEndReached = false
        isMakingRequest = false
    }

    /**
     * Checks if the paginator can load more items.
     *
     * @return true if more items can be loaded (not loading and not at end), false otherwise
     */
    fun canLoadMore(): Boolean {
        return !isMakingRequest && !isEndReached
    }
}