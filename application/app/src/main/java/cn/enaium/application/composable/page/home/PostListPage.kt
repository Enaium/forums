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

package cn.enaium.application.composable.page.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.enaium.application.composable.component.push
import cn.enaium.application.composable.component.topBar
import cn.enaium.application.model.Model
import cn.enaium.application.model.result.Category
import cn.enaium.application.model.result.Post
import cn.enaium.application.util.Http
import cn.enaium.application.util.execute
import kotlinx.coroutines.launch

/**
 * @author Enaium
 */
@Composable
fun postListPage(model: Model) {
    val current = LocalContext.current

    fun refresh() {
        execute {
            model.postList.addAll(Http.tReturn<List<Post>>("post/getByUpdate/0/10").data)
        }
    }

    rememberCoroutineScope().launch {
        refresh()
    }

    Button({
        model.currentPostIndex = 0
        model.postList.clear()
        refresh()
    }) {
        Text("Refresh")
    }

    Column(Modifier.fillMaxSize()) {
        topBar(model, "Post")
        LazyColumn(Modifier.fillMaxSize()) {
            if (!model.postList.isEmpty()) {
                itemsIndexed(model.postList) { index, item ->
                    postItem(item) {
                        model.rememberNavController!!.push("post/${item.id}")
                    }
                    if (index < model.postList.lastIndex) {
                        Divider(
                            startIndent = 68.dp,
                            thickness = 0.8f.dp
                        )
                    } else {
                        Button({
                            model.currentPostIndex += 10
                            execute {
                                val data =
                                    Http.tReturn<ArrayList<Post>>("post/getByUpdate/${model.currentPostIndex}/10").data
                                it.post {
                                    if (data.isNotEmpty()) {
                                        model.postList.addAll(data)
                                    } else {
                                        Toast.makeText(current, "No more", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }, Modifier.fillMaxWidth()) {
                            Text("more...")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun postItem(post: Post, onSelectedChanged: (Long) -> Unit) {

    var category: Category? by remember { mutableStateOf(null) }
    rememberCoroutineScope().launch {
        execute { category = Http.tReturn<Category>("category/get/${post.category}").data }
    }

    Column(Modifier.fillMaxWidth().clickable {
        onSelectedChanged(post.id)
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(post.title, fontSize = 24.sp)
            Box(Modifier.weight(1f))
            Text(post.updateTime.toString(), fontSize = 11.sp)
        }

        Row {
            if (category != null) {
                Text(category!!.title)
            }
        }
    }
}