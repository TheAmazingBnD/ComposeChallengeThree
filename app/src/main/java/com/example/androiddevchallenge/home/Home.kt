package com.example.androiddevchallenge.home

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.redditposts.RedditPostList
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.viewmodel.HomeViewModel

@Composable
fun Home(
    darkTheme: Boolean,
    openRedditPost: (String) -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel()
    val postList = homeViewModel.currentViewState.posts.collectAsState()

    MyTheme(darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Home") },
                    elevation = 4.dp
                )
            },
            content = {
                RedditPostList(
                    posts = postList.value,
                    openRedditPost = openRedditPost,
                    loadMore = { homeViewModel.fetchNextPage() }
                )
            }
        )
    }
}
