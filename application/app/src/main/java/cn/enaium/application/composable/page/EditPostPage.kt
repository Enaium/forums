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

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.enaium.application.composable.component.topBar
import cn.enaium.application.model.Model
import cn.enaium.application.composable.component.push
import cn.enaium.application.model.result.Category
import cn.enaium.application.model.result.Return
import cn.enaium.application.util.Http
import cn.enaium.application.util.execute
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author Enaium
 */
@Composable
fun editPost(model: Model) {
    val context = LocalContext.current
    var title: String by remember { mutableStateOf(model.title) }
    var content: String by remember { mutableStateOf(model.content) }

    var list by remember { mutableStateOf(listOf<Category>()) }

    rememberCoroutineScope().launch {
        execute { list = Http.tReturn<List<Category>>("category/get").data }
    }

    var expanded by remember { mutableStateOf(false) }

    var category: Category? by remember { mutableStateOf(null) }

    Column(Modifier.fillMaxSize()) {
        topBar(model, "EditPost") {
            model.rememberNavController!!.push("home")
            model.content = ""
            model.title = ""
        }
        Button(onClick = { model.rememberNavController!!.push("markdownView") }, Modifier.fillMaxWidth()) {
            Text("View Markdown")
        }
        Text("Title:")
        TextField(title, {
            title = it
            model.title = it
        }, Modifier.fillMaxWidth())
        Text("Category:")
        Button(onClick = {
            expanded = true
        }) {
            Text("${category?.title}")
        }
        DropdownMenu(expanded, onDismissRequest = {

        }) {
            list.forEach {
                DropdownMenuItem(onClick = {
                    category = it
                    expanded = false
                }) {
                    Text(it.title)
                }
            }
        }
        Text("Content:")
        TextField(content, {
            content = it
            model.content = it
        }, Modifier.fillMaxWidth().weight(1f))
        Button(onClick = {
            if (title.isBlank()) {
                Toast.makeText(context, "Please input title", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if (content.isBlank()) {
                Toast.makeText(context, "Please input content", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if (category == null) {
                Toast.makeText(context, "Please select category", Toast.LENGTH_SHORT).show()
                return@Button
            }


            execute {
                val tReturn = Http.tReturn<String>(
                    "post/new-post", mapOf(
                        "title" to title,
                        "content" to content,
                        "category" to category!!.id
                    )
                )

                it.post {
                    if (tReturn.success) {
                        model.title = ""
                        model.content = ""
                        model.rememberNavController!!.popBackStack()
                    }
                    Toast.makeText(context, tReturn.data, Toast.LENGTH_SHORT).show()
                }
            }

        }, Modifier.fillMaxWidth()) {
            Text("Post")
        }
    }
}