package com.example.androiddevchallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.network.RedditApiClient
import com.example.androiddevchallenge.network.models.ProgressType
import com.example.androiddevchallenge.network.models.data.ApiRedditCommentPost
import com.example.androiddevchallenge.network.models.data.ApiRedditPost
import com.example.androiddevchallenge.network.models.data.toRedditPost
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RedditPostViewModel(private val apiClient: RedditApiClient = RedditApiClient()) :
    ViewModel() {

    data class ViewState(
        val progressType: ProgressType,
        val comments: List<ApiRedditCommentPost>,
        val post: ApiRedditPost,
        val commentCount: Int
    )

    private val viewState = MutableStateFlow(
        ViewState(
            progressType = ProgressType.NotAsked,
            comments = emptyList(),
            post = ApiRedditPost(),
            commentCount = 0
        )
    )

    fun fetchPage(permalink: String) {
        viewModelScope.launch {
            updateState(
                currentViewState.value.copy(
                    progressType = ProgressType.Loading
                )
            )
            val response = apiClient.getApiService()
                .getPage(permalink)
            flowOf(response)
                .distinctUntilChanged()
                .collect {
                    when {
                        it.isSuccessful -> {
                            val redditPostResponse = it.body()
                            redditPostResponse?.let { postPageWrapper ->
                                postPageWrapper.let { listOfPostData ->
                                    var redditPost = ApiRedditPost()

                                    listOfPostData.first().data?.children?.forEach { post ->
                                        redditPost = post.toRedditPost()
                                    }

                                    val comments = currentViewState.value.comments.plus(
                                        listOfPostData[1]
                                        .data?.children ?: emptyList()
                                    )

                                    updateState(
                                        currentViewState.value.copy(
                                            progressType = ProgressType.Result(listOfPostData),
                                            post = redditPost,
                                            comments = comments,
                                            commentCount = comments.size
                                        )
                                    )
                                }
                            } ?: updateState(
                                currentViewState.value.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                            )
                            // Temp
                        }
                        else -> {
                            updateState(
                                currentViewState.value.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                            )
                            // Temp
                        }
                    }
                }
        }
    }

    val currentViewState = viewState

    private fun updateState(newState: ViewState) {
        viewState.value = currentViewState.value
            .copy(
                progressType = newState.progressType,
                comments = newState.comments,
                post = newState.post,
                commentCount = newState.commentCount
            )
    }
}
