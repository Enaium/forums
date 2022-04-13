/**
 * Copyright (c) 2022 Enaium
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.enaium.application.composable.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.composable
import cn.enaium.application.composable.page.*
import cn.enaium.application.model.Model
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @author Enaium
 */

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun nav(model: Model) {
    model.rememberNavController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = model.rememberNavController!!,
        startDestination = "home",
        enterTransition = {
            slideInHorizontally(animationSpec = tween(500)) {
                -it
            }
        },
        exitTransition = {
            slideOutHorizontally(animationSpec = tween(500)) {
                it
            }
        }
    ) {
        composable("home") {
            home(model)
        }

        composable("editPost") {
            editPost(model)
        }

        composable("markdownView") {
            markdownView(model)
        }

        composable("memberInfo/{id}/{edit}") {
            val id = it.arguments?.getString("id")!!.toLong()
            val edit = it.arguments?.getString("edit")!!.toBoolean()
            memberInfo(model, id, edit)
        }

        composable("post/{id}") {
            val id = it.arguments?.getString("id")!!.toLong()
            post(model, id)
        }
    }
}

fun NavController.push(route: String) {
    navigate(route)
}