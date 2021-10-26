package com.example.androiddevchallenge.network.models.data

data class ApiRedditPostPageWrapper(val kind: String? = null, val data: ApiRedditPostPageData? = null)

data class ApiRedditPostPageData(
    val modhash: String? = null,
    val dist: Int? = null,
    val children: List<ApiRedditCommentPost>? = null,
    val after: String? = null,
    val before: String? = null
)
