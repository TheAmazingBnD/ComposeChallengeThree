package com.example.androiddevchallenge.navigation

/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Route(val path: String) {
    object Home : Route("home")
    object RedditPost : Route("post/{link}") {
        val idArgument = "link"
        fun pathWithId(link: String) = path.replace("{link}", link)
    }
}

class NavigationActions(navController: NavController) {
    val openDetail: (String) -> Unit = { link ->
        val encodedLink = URLEncoder.encode(link, StandardCharsets.UTF_8.toString())
        navController.navigate(Route.RedditPost.pathWithId(encodedLink))
    }

    val navigateUp: () -> Unit = {
        navController.popBackStack()
    }
}