package com.example.androiddevchallenge.network.models

sealed class ProgressType(open val data: Any? = null) {

    object NotAsked: ProgressType()

    object Loading: ProgressType()

    data class Result(override val data: Any): ProgressType(data)

    data class Failure(override val data: Any): ProgressType(data)

}