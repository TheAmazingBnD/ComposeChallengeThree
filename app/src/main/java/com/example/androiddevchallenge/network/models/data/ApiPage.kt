package com.example.androiddevchallenge.network.models.data

data class ApiPageWrapper(val kind: String? = null, val data: ApiPageData? = null)

data class ApiPageData(
    val modhash: String? = null,
    val dist: Int? = null,
    val children: List<ApiRedditPost>? = null,
    val after: String? = null,
    val before: String? = null
)
