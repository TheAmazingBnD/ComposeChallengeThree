package com.example.androiddevchallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.network.RedditApiClient
import com.example.androiddevchallenge.network.models.ProgressType
import com.example.androiddevchallenge.network.models.data.ApiPageWrapper
import com.example.androiddevchallenge.network.models.data.ApiRedditPost
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(
    private val apiClient: RedditApiClient = RedditApiClient()
) : ViewModel() {

    data class ViewState(
        val progressType: ProgressType,
        val posts: MutableStateFlow<List<ApiRedditPost>>,
        val count: Int,
        val after: String?,
        val before: String?
    )

    private var viewState = MutableStateFlow(
        ViewState(
            progressType = ProgressType.NotAsked,
            posts = MutableStateFlow(emptyList()),
            count = 0,
            after = null,
            before = null
        )
    )

    val currentViewState: ViewState = viewState.value

    init {
        fetchHomePage()
    }

    fun fetchHomePage() {
        updateState(
            currentViewState.copy(
                progressType = ProgressType.Loading
            )
        )
        viewModelScope.launch {
            val response = apiClient.getApiService()
                .getHomePage()
            flowOf(response)
                .collect {
                    when {
                        it.isSuccessful -> {
                            val homePage = it.body()
                            homePage?.let { pageWrapper ->
                                val redditPosts = currentViewState.posts.value
                                val updatedList = redditPosts.plus(
                                    pageWrapper.data?.children ?: emptyList()
                                )

                                currentViewState.posts.value = updatedList

                                updateState(
                                    currentViewState.copy(
                                        progressType = ProgressType.Result(pageWrapper),
                                        count = updatedList.size,
                                        after = pageWrapper.data?.after,
                                        before = pageWrapper.data?.before
                                    )
                                )
                            } ?: updateState(
                                currentViewState.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                                // Temp
                            )
                        }
                        else -> {
                            // Temp

                            updateState(
                                currentViewState.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                            )
                        }
                    }
                }
        }
    }

    fun fetchNextPage() {
        updateState(
            currentViewState.copy(
                progressType = ProgressType.Loading
            )
        )
        viewModelScope.launch {
            val response = apiClient.getApiService()
                .getNextPage(currentViewState.count.toString(), currentViewState.after)
            flowOf(response)
                .distinctUntilChanged()
                .collect {
                    when {
                        it.isSuccessful -> {
                            val redditPostsResponse = it.body()
                            redditPostsResponse?.let { pageWrapper ->
                                val redditPosts = currentViewState.posts.value
                                val updatedList = redditPosts.plus(
                                    pageWrapper.data?.children ?: emptyList()
                                )

                                currentViewState.posts.value = updatedList

                                updateState(
                                    currentViewState.copy(
                                        progressType = ProgressType.Result(pageWrapper),
                                        count = updatedList.size,
                                        after = pageWrapper.data?.after,
                                        before = pageWrapper.data?.before
                                    )
                                )
                            } ?: updateState(
                                currentViewState.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                                // Temp
                            )
                        }
                        else -> {
                            // Temp
                            updateState(
                                currentViewState.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun updateState(newState: ViewState) {
        viewState.value = currentViewState
            .copy(
                progressType = newState.progressType,
                posts = newState.posts,
                count = newState.count,
                after = newState.after,
                before = newState.before
            )
    }
}
