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
        val comments: MutableStateFlow<List<ApiRedditCommentPost>>,
        val post: MutableStateFlow<ApiRedditPost>,
        val commentCount: Int
    )

    private val viewState = MutableStateFlow(
        ViewState(
            progressType = ProgressType.NotAsked,
            comments = MutableStateFlow(emptyList()),
            post = MutableStateFlow(ApiRedditPost()),
            commentCount = 0
        )
    )

    fun fetchPage(permalink: String) {
        viewModelScope.launch {
            updateState(
                currentViewState.copy(
                    progressType = ProgressType.Loading
                )
            )
            val response = apiClient.getApiService()
                .getPage(permalink)
            flowOf(response)
                .collect {
                    when {
                        it.isSuccessful -> {
                            val redditPostResponse = it.body()
                            redditPostResponse?.let { postPageWrapper ->
                                postPageWrapper.let { listOfPostData ->
                                    var redditPost = ApiRedditPost()

                                    postPageWrapper.first().data?.children?.forEach { post ->
                                        redditPost = post.toRedditPost()
                                    }

                                    val comments = currentViewState.comments.value.plus(
                                        postPageWrapper[1]
                                        .data?.children ?: emptyList()
                                    )

                                    currentViewState.comments.value = comments

                                    updateState(
                                        currentViewState.copy(
                                            progressType = ProgressType.Result(listOfPostData),
                                            post = MutableStateFlow(redditPost),
                                            commentCount = comments.size
                                        )
                                    )
                                }
                            } ?: updateState(
                                currentViewState.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                            )
                            // Temp
                        }
                        else -> {
                            updateState(
                                currentViewState.copy(
                                    progressType = ProgressType.Failure(it)
                                )
                            )
                            // Temp
                        }
                    }
                }
        }
    }

    val currentViewState: ViewState = viewState.value

    private fun updateState(newState: ViewState) {
        viewState.value = currentViewState
            .copy(
                progressType = newState.progressType,
                comments = newState.comments,
                post = newState.post,
                commentCount = newState.commentCount
            )
    }
}
