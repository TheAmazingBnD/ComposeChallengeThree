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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.plusAssign
import com.example.androiddevchallenge.home.Home
import com.example.androiddevchallenge.navigation.NavigationActions
import com.example.androiddevchallenge.navigation.Route
import com.example.androiddevchallenge.network.models.data.ApiRedditPost
import com.example.androiddevchallenge.redditpost.RedditPostBottomSheetLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

@ExperimentalMaterialNavigationApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(true)
            }
        }
    }
}

// Start building your app here!
@Composable
@ExperimentalMaterialNavigationApi
fun MyApp(darkTheme: Boolean) {
    val navController = rememberNavController()
    val navActions = remember(navController) { NavigationActions(navController) }

    MyTheme(darkTheme = darkTheme) {
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        navController.navigatorProvider += bottomSheetNavigator
        ModalBottomSheetLayout(bottomSheetNavigator) {
            NavHost(navController, startDestination = Route.Home.path) {
                composable(Route.Home.path) {
                    Home(
                        darkTheme = darkTheme,
                        openRedditPost = navActions.openDetail
                    )
                }
                bottomSheet(Route.RedditPost.path) { backStackEntry ->
                    val link = backStackEntry.arguments?.getString(Route.RedditPost.idArgument)
                    requireNotNull(link)

                    RedditPostBottomSheetLayout(darkTheme, link, navActions.navigateUp)
                }
            }
        }
    }
}

@ExperimentalMaterialNavigationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme(darkTheme = false) {
        MyApp(false)
    }
}

@ExperimentalMaterialNavigationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(true)
    }
}
