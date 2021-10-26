package com.example.androiddevchallenge.redditpost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.home.Home
import com.example.androiddevchallenge.network.RedditApiClient
import com.example.androiddevchallenge.network.models.data.ApiRedditCommentPost
import com.example.androiddevchallenge.redditposts.*
import com.example.androiddevchallenge.redditposts.RedditPostListItem
import com.example.androiddevchallenge.viewmodel.RedditPostViewModel

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun RedditPostBottomSheetLayout(darkTheme: Boolean, link: String, navigateUp: () -> Unit) {
    val redditPostViewModel: RedditPostViewModel = viewModel()
    redditPostViewModel.fetchPage(link)
    val comments = redditPostViewModel.currentViewState.comments.collectAsState()
    val post = redditPostViewModel.currentViewState.post.collectAsState()

    RedditPostListItem(
        post = post.value,
        openRedditPost = { },
    )
    Divider(
        modifier = Modifier.background(Color.White)
    )
    RedditCommentList(
        posts = comments.value,
        openRedditPost = {}
    ) { }
}

@Composable
fun RedditCommentList(
    modifier: Modifier = Modifier,
    posts: List<ApiRedditCommentPost>,
    openRedditPost: (String) -> Unit,
    loadMore: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier,
    ) {
        val listState = rememberLazyListState()

        LazyColumn(modifier.fillMaxWidth(), listState) {
            items(posts) { post ->
                RedditCommentItem(modifier, post, openRedditPost)
            }
        }

        listState.InfiniteListHandler {
            loadMore()
        }
    }
}

@Composable
fun RedditCommentItem(
    modifier: Modifier = Modifier,
    post: ApiRedditCommentPost,
    openRedditPost: (String) -> Unit
) {
    val postData = post.data

    postData?.let {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { openRedditPost(postData?.permalink!!) },
            elevation = 3.dp
        ) {
            Column {
                RedditPostBody(
                    author = postData.author ?: "",
                    subreddit = postData.subreddit ?: "",
                    timePosted = getTimeAgo(postData.created ?: 0L),
                    title = postData.title ?: "",
                    body = postData.body ?: "",
                    link = postData.url ?: "",
                    upVoteCount = postData.ups ?: 0,
                    imageUrl = postData.thumbnail ?: ""
                )
            }
        }
    }
}
