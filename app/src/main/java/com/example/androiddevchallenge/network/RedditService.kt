package com.example.androiddevchallenge.network

import com.example.androiddevchallenge.network.models.data.ApiPageWrapper
import com.example.androiddevchallenge.network.models.data.ApiRedditPostPageWrapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditService {
    @GET("/{filter}/.json")
    suspend fun getFilter(@Path("filter") filter: String) : Response<ApiPageWrapper>

    @GET(".json")
    suspend fun getHomePage() : Response<ApiPageWrapper>

    @GET(".json")
    suspend fun getNextPage(
        @Query("count") count: String?,
        @Query("after") after: String?
    ) : Response<ApiPageWrapper>

    @GET("/{filter}/.json")
    suspend fun getNextPageWithFilter(@Path("filter") filter: String?, @Query("count") count: String?, @Query("after") after: String?) : Response<ApiPageWrapper>

    @GET("{permalink}.json")
    suspend fun getPage(@Path(value = "permalink", encoded = true) permalink: String?) : Response<List<ApiRedditPostPageWrapper>>

    @GET("/search.json")
    suspend fun search(@Query("q") query: String) : Response<ApiPageWrapper>
}
