package com.example.androiddevchallenge.redditposts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.androiddevchallenge.network.models.data.ApiRedditPost
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RedditPostList(
    modifier: Modifier = Modifier,
    posts: List<ApiRedditPost>,
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
                RedditPostListItem(modifier, post, openRedditPost)
        }
    }

    listState.InfiniteListHandler {
        loadMore()
    }
}
}

@Composable
fun RedditPostListItem(
    modifier: Modifier = Modifier,
    post: ApiRedditPost,
    openRedditPost: (String) -> Unit
) {
    val postData = post.data

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
                author = postData?.author ?: "",
                subreddit = postData?.subreddit ?: "",
                timePosted = getTimeAgo(postData?.created ?: 0L),
                title = postData?.title ?: "",
                body = postData?.selftext ?: "",
                link = postData?.url ?: "",
                upVoteCount = postData?.ups ?: 0,
                imageUrl = postData?.thumbnail ?: ""
            )
        }
    }
}

@Composable
fun RedditPostImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier,
    ) {
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "Friend Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(24.dp)
                .height(150.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
fun RedditPostBody(
    modifier: Modifier = Modifier,
    author: String,
    subreddit: String,
    timePosted: String,
    title: String,
    body: String,
    link: String,
    upVoteCount: Int,
    imageUrl: String
) {
    Text(
        text = "u/: ",
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Text(
        text = author,
        modifier = Modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp)
    )
    Divider()
    Text(
        text = "r/: ",
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Text(
        text = subreddit,
        modifier = Modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp)
    )
    Divider()
    Text(
        text = "TimePosted: ",
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Text(
        text = timePosted,
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Divider()
    Text(
        text = "Title: ",
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Text(
        text = title,
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Divider()
    RedditPostImage(
        imageUrl = imageUrl
    )
    Divider()
    Text(
        text = "Body: ",
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Text(
        text = body,
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Divider()
    Text(
        text = "Link: ",
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Text(
        text = link,
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Divider()
    Text(
        text = "UpVotes: ",
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Text(
        text = upVoteCount.toString(),
        modifier = modifier.padding(bottom = 4.dp, top = 4.dp, end = 4.dp, start = 4.dp),
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
    Divider()
}

@Composable
fun LazyListState.InfiniteListHandler(
    onLoadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore){
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) onLoadMore()
            }
    }
}

fun getTimeAgo(time: Long): String {
    val date = Date(time * 1000)
    val format = SimpleDateFormat("hh:mm MM/dd/yyyy", Locale.getDefault())
    return format.format(date)
}
