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

package cn.enaium.application.composable.page

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cn.enaium.application.composable.component.bottomBar
import cn.enaium.application.composable.page.home.categoryPage
import cn.enaium.application.composable.page.home.mePage
import cn.enaium.application.composable.page.home.postListPage
import cn.enaium.application.model.Model
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 * @author Enaium
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun home(model: Model) {
    Column {
        val pagerState = rememberPagerState()
        HorizontalPager(3, Modifier.weight(1f), pagerState) {
            when (it) {
                0 -> postListPage(model)
                1 -> categoryPage(model)
                2 -> mePage(model)
            }
        }
        val scope = rememberCoroutineScope()
        bottomBar(pagerState.currentPage) {
            scope.launch {
                pagerState.animateScrollToPage(it)
            }
        }
    }
}