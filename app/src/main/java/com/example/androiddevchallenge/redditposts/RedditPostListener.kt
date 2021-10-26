package com.example.androiddevchallenge.redditposts

import com.example.androiddevchallenge.network.models.data.ApiRedditPost

interface RedditPostListener {
    fun fetchNextPage()
    fun postClicked(post: ApiRedditPost)
    fun getCount() : Int?
}
